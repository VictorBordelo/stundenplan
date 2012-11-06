package edu.hm.stundenplan;

import static org.junit.Assert.*;

import java.io.*;
import java.net.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;
import org.junit.*;

import edu.hm.*;

public class VelocityCssTest {

    private static Server jetty;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jetty = new Server(JettyStarterForStundenplan.PORT);
        jetty.setHandler(new WebAppContext(JettyStarterForStundenplan.WEBAPP_DIR, JettyStarterForStundenplan.APP_URL));
        jetty.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jetty.stop();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        URL url;
        url = new URL("http", "localhost", JettyStarterForStundenplan.PORT, "/knockout/styleForVelocityTest.css");
        InputStream stream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer result = new StringBuffer();
        while(reader.ready()) {
            result.append(reader.readLine());
        }
        assertEquals(".button {    height: 3em;    width: 8em;      border-radius: 10px;}", result.toString());
    }

}
