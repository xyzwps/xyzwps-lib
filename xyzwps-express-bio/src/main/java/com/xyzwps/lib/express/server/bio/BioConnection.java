package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import com.xyzwps.lib.express.server.bio.common.ContentLengthInputStream;
import com.xyzwps.lib.express.commons.KeepAliveConfig;
import com.xyzwps.lib.express.commons.KeepAliveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BioConnection implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(BioConnection.class);

    private static final AtomicInteger id_counter = new AtomicInteger(0);

    private final Socket socket;
    private final Filter filter;

    private KeepAliveInfo keepAliveInfo;

    private final int connectionId;
    private int connectionRequestCount = 0;
    private static final ConnectionManager cm = new ConnectionManager();

    private final ReentrantLock loopLock = new ReentrantLock();

    BioConnection(Socket socket, Filter filter) {
        this.socket = Args.notNull(socket, "Socket cannot be null");
        this.filter = filter == null ? Filter.empty() : filter;
        this.connectionId = id_counter.getAndIncrement();
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

                boolean keepAlive;
                if (loopLock.tryLock()) {
                    keepAlive = this.handleOneLoop(in, out);
                    loopLock.unlock();
                } else {
                    break;
                }
                if (!keepAlive) break;
            }
        } catch (SocketException e) {
            if ("Connection reset".equalsIgnoreCase(e.getMessage())) {
                log.info("==> socket closed by client");
            } else {
                log.error("Handle socket error", e);
            }
        } catch (IOException | UncheckedIOException e) {
            log.error("Handle socket error", e);
        } catch (BadProtocolException e) {
            log.error("Bad protocol error", e);
        } catch (Exception e) {
            log.error("Unhandled error", e);
        } finally {
            log.info("==> socket {} has been closed.", connectionId);
            cm.rm(this);
        }
    }

    /**
     * @return true if socket should keep alive
     */
    private boolean handleOneLoop(InputStream in, OutputStream out) throws IOException {
        this.connectionRequestCount++;
        var requestParser = new RawRequestParser(in);
        var startLine = requestParser.startLine()
                .peekLeft(log::error)
                .rightOrThrow(BadProtocolException::new);

        var headers = requestParser.headers()
                .peekLeft(log::error)
                .rightOrThrow(BadProtocolException::new);

        // region check keep alive
        if (this.keepAliveInfo == null) {
            var values = headers.getAll(HttpHeaders.CONNECTION);
            if (values == null || values.isEmpty() || values.stream().anyMatch(it -> it.equalsIgnoreCase("Keep-Alive"))) {
                var keepAliveConfig = new KeepAliveConfig(30, 100);
                this.keepAliveInfo = new KeepAliveInfo(keepAliveConfig);
                KEEP_ALIVE_TIMER.schedule(closeSocketAfterKeepAliveTimeout(), keepAliveConfig.timeout() * 1000L); // TODO: 乱七八糟，想想办法再
            }
        }
        // endregion

        var contentLength = headers.contentLength();
        InputStream requestBody = contentLength == 0
                ? InputStream.nullInputStream()
                : new ContentLengthInputStream(in, contentLength);

        var request = new BioHttpRequest(startLine.method(), startLine.toURI(), startLine.protocol(), headers, requestBody);
        var response = new BioHttpResponse(out, request.protocol());

        var responseHeaders = response.headers();

        var shouldKeepAlive = keepAliveInfo != null && keepAliveInfo.shouldKeepAlive();
        // region set keep alive header
        if (shouldKeepAlive) {
            responseHeaders.append(HttpHeaders.CONNECTION, "Keep-Alive");
            responseHeaders.append(HttpHeaders.KEEP_ALIVE, keepAliveInfo.toHeaderValue());
        } else {
            responseHeaders.append(HttpHeaders.CONNECTION, "Close");
        }
        // endregion

        log.info("==> {} - {}", connectionId, connectionRequestCount);

        filter.filter(request, response, Filter.Next.empty());
        exhaust(requestBody);

        // region check connection should keep alive
        return shouldKeepAlive;
        // endregion
    }

    private TimerTask closeSocketAfterKeepAliveTimeout() {
        return new TimerTask() {
            @Override
            public void run() {
                if (loopLock.tryLock()) {
                    try (socket) {
                        log.info("==> socket {} has been closed by keep alive timeout.", connectionId);
                    } catch (IOException e) {
                        log.error("Handle socket error", e);
                    } finally {
                        cm.rm(BioConnection.this);
                    }
                    loopLock.unlock();
                }
            }
        };
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
        private final ConcurrentHashMap<Integer, BioConnection> connections = new ConcurrentHashMap<>();

        void add(BioConnection handler) {
            this.connections.put(handler.connectionId, handler);
        }

        int count() {
            return connections.size();
        }

        void rm(BioConnection handler) {
            connections.remove(handler.connectionId);
        }
    }


    private static final Timer KEEP_ALIVE_TIMER = new Timer();
}
