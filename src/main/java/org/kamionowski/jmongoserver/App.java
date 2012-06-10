package org.kamionowski.jmongoserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {

    private static final int PORT = 10000;
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        JMongoServer server = null;
        try {
            log.info("Starting...");
            server = new JMongoServer(PORT);
            server.listen();
        } catch (Exception exc) {
            log.error("Error", exc);
            if (server != null) {
                server.dispose();
            }
        }
    }
}
