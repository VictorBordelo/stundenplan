package edu.hm.extjs;

import org.apache.commons.logging.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;
import org.junit.*;

@Ignore
public class BasicServiceTest {

    protected static final String APP_URL = "/Ext";
    protected static final String WEBAPP_DIR = "./src/main/webapp/";
    protected static final int PORT = 8088;
    protected static Log logExtJS = LogFactory.getLog(BasicServiceTest.class);
    
    @BeforeClass
    public static void setUp() throws Exception {
        Server jetty = new Server(PORT);
        jetty.setHandler(new WebAppContext(WEBAPP_DIR, APP_URL));
        jetty.start();
    }
    
}
