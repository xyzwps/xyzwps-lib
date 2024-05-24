package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.server.bio.common.ContentLengthInputStream;
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
    private boolean keepAlive;
    private final KeepAliveConfig keepAliveConfig = new KeepAliveConfig(30, 1000);
    private final AtomicInteger keepAliveCounter = new AtomicInteger(0);


    private final int id;
    private static final ConnectionManager cm = new ConnectionManager();


    ConnectionHandler(Socket socket, HttpMiddleware middleware) {
        this.socket = Args.notNull(socket, "Socket cannot be null");
        this.middleware = Args.notNull(middleware, "HttpMiddleware cannot be null");
        this.keepAlive = false;
        this.id = id_counter.getAndIncrement();
    }

    @Override
    public void run() {
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
                if (isKeepAlive(headers)) {
                    this.keepAlive = true;
                }
                // endregion

                var contentLength = headers.contentLength();
                InputStream requestBody = contentLength == 0
                        ? InputStream.nullInputStream()
                        : new ContentLengthInputStream(in, 2048, contentLength);

                var request = new BioHttpRequest(startLine.method(), startLine.toURI(), startLine.protocol(), headers, requestBody);
                var response = new BioHttpResponse(out, request.protocol());

                // region set keep alive header
                int usedCount = keepAliveCounter.incrementAndGet();
                if (usedCount < keepAliveConfig.max()) {
                    response.headers().append(HttpHeaders.KEEP_ALIVE, keepAliveConfig.toHeaderValue(usedCount));
                }
                // endregion

                middleware.call(HttpContext.start(request, response));

                exhaust(requestBody);

                // region check connection should keep alive
                if (!keepAlive || usedCount >= keepAliveConfig.max()) {
                    break;
                } else {
                    socket.setSoTimeout(keepAliveConfig.timeout() * 1000);
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


    private static boolean isKeepAlive(HttpHeaders headers) {
        return headers.getAll(HttpHeaders.CONNECTION).stream()
                .anyMatch(it -> it.equalsIgnoreCase("Keep-Alive"));
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
