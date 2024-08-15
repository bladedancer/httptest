package org.matthews;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class Server {
    private final int port;
    private final int backlog;
    private final long delay;
    private HttpServer httpServer;

    public void start() {
        log.info("Starting HTTP Server on {}", port);
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
            httpServer.createContext("/hello", new HelloHandler(delay));
            httpServer.setExecutor(Executors.newCachedThreadPool());
            httpServer.start();
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void stop() {
        log.info("Stopping HTTP Server");
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @RequiredArgsConstructor
    public static class HelloHandler implements HttpHandler {
        private AtomicInteger count = new AtomicInteger(0);
        private final long delay;

        @Override
        @SneakyThrows
        public void handle(HttpExchange exchange) throws IOException {
            int c = count.incrementAndGet();
            log.info("Handling Request {}", c);
            Thread.sleep(delay);
            String response = "Hello world";
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            log.info("Handled Request  {}", c);
        }
    }
}
