package edu.hm;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;

public class JettyStarterForStundenplan {
    public static final String APP_URL = "/";
    public static final String WEBAPP_DIR = "./src/main/webapp/";
    public static final int PORT = 8081;

    public static void main(String... args) throws Exception {
        Server jetty = new Server(PORT);
        jetty.setHandler(new WebAppContext(WEBAPP_DIR, APP_URL));
        jetty.start();
        System.out.println("Jetty listening on localhost:" + PORT + APP_URL);
        jetty.join();
    }
}
