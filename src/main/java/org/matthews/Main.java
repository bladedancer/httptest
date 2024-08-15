package org.matthews;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "server".equalsIgnoreCase(args[0])) {
            log.info("Server mode");
            Server server = new Server(8080, Integer.MAX_VALUE, 50);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    server.stop();
                }
            });
        } else {
            log.info("Client mode");
        }
    }
}