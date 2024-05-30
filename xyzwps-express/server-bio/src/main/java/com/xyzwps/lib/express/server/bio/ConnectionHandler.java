package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.server.bio.common.ContentLengthInputStream;
import com.xyzwps.lib.express.server.commons.KeepAliveConfig;
import com.xyzwps.lib.express.server.commons.KeepAliveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
    private static final AtomicInteger id_counter = new AtomicInteger(0);

    private final Socket socket;
    private final HttpMiddleware middleware;

    // TODO: 配置化 keep alive 信息，区分主动和被动
    private KeepAliveInfo keepAliveInfo;

    private final int id;
    private static final ConnectionManager cm = new ConnectionManager();


    ConnectionHandler(Socket socket, HttpMiddleware middleware) {
        this.socket = Args.notNull(socket, "Socket cannot be null");
        this.middleware = Args.notNull(middleware, "HttpMiddleware cannot be null");
        this.id = id_counter.getAndIncrement();
    }

    @Override
    public void run() {
        // TODO: 这里 keep alive 不大对
        try (socket;
             var in = new PushbackInputStream(socket.getInputStream(), 1);
             var out = socket.getOutputStream()
        ) {
            cm.add(this);

            int firstByte;
            while ((firstByte = in.read()) >= 0) {
                in.unread(firstByte);

                var requestParser = new RawRequestParser(in);

                var startLine = requestParser.startLine()
                        .peekLeft(log::error)
                        .rightOrThrow(BadProtocolException::new);


                var headers = requestParser.headers()
                        .peekLeft(log::error)
                        .rightOrThrow(BadProtocolException::new);

                // region check keep alive
                if (headers.connectionKeepAlive()) {
                    this.keepAliveInfo = new KeepAliveInfo(new KeepAliveConfig(30, 1000));
                }
                // endregion

                var contentLength = headers.contentLength();
                InputStream requestBody = contentLength == 0
                        ? InputStream.nullInputStream()
                        : new ContentLengthInputStream(in, 2048, contentLength);

                var request = new BioHttpRequest(startLine.method(), startLine.toURI(), startLine.protocol(), headers, requestBody);
                var response = new BioHttpResponse(out, request.protocol());

                // region set keep alive header
                if (keepAliveInfo != null && keepAliveInfo.shouldKeepAlive()) {
                    response.headers().append(HttpHeaders.KEEP_ALIVE, keepAliveInfo.toHeaderValue());
                }
                // endregion

                middleware.call(HttpContext.start(request, response));

                exhaust(requestBody);

                // region check connection should keep alive
                if (keepAliveInfo != null && keepAliveInfo.shouldKeepAlive()) {
                    break;
                } else {
                    socket.close();
                }
                // endregion
            }
        } catch (IOException | UncheckedIOException e) {
            log.error("Handle socket error", e);
        } catch (BadProtocolException e) {
            log.error("Bad protocol error", e);
        } catch (Exception e) {
            log.error("Unhandled error", e);
        } finally {
            cm.rm(this);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static void exhaust(InputStream in) {
        try (in) {
            while (in.read() >= 0) ; // TODO: 可能需要优化
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static final class ConnectionManager {
        private final ConcurrentHashMap<Integer, ConnectionHandler> connections = new ConcurrentHashMap<>();

        void add(ConnectionHandler handler) {
            this.connections.put(handler.id, handler);
        }

        int count() {
            return connections.size();
        }

        void rm(ConnectionHandler handler) {
            connections.remove(handler.id);
        }
    }
}
