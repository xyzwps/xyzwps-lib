package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.BadProtocolException;
import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.server.craft.common.ContentLengthInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UncheckedIOException;
import java.net.Socket;

public class SocketHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    private final Socket socket;
    private final HttpMiddleware middleware;

    SocketHandler(Socket socket, HttpMiddleware middleware) {
        this.socket = Args.notNull(socket, "Socket cannot be null");
        this.middleware = Args.notNull(middleware, "HttpMiddleware cannot be null");
    }

    @Override
    public void run() {
        try (socket;
             var in = new PushbackInputStream(socket.getInputStream(), 1);
             var out = socket.getOutputStream()
        ) {
            int firstByte;
            while ((firstByte = in.read()) >= 0) {
                in.unread(firstByte);

                var requestParser = new RawRequestParser(in);

                var startLine = requestParser.startLine();
                var headers = requestParser.headers(); // TODO: 处理 BadProtocolException
                var contentLength = headers.contentLength();

                // TODO: 处理 keep-alive header

                InputStream requestBody = contentLength == 0 ? InputStream.nullInputStream() : new ContentLengthInputStream(in, 2048, contentLength);

                var request = new CraftHttpRequest(startLine.method(), startLine.toURI(), startLine.protocol(), headers, requestBody);
                var response = new CraftHttpResponse(out, request);
                middleware.call(HttpContext.start(request, response));

                exhaust(requestBody);
            }
        } catch (IOException | UncheckedIOException e) {
            log.error("Handle socket error", e);
        } catch (BadProtocolException e) {
            log.error("Bad protocol error", e);
        } catch (Exception e) {
            log.error("Unhandled error", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static void exhaust(InputStream in) {
        try (in) {
            while (in.read() >= 0) ;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
