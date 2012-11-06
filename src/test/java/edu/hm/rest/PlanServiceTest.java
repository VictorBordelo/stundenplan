package edu.hm.rest;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;
import org.junit.*;

public class PlanServiceTest extends BasicRestTest {

    private static final String URL = "http://localhost:" + PORT + APP_URL + "/rest/json/plan";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jetty = new Server(PORT);
        jetty.setHandler(new WebAppContext(WEBAPP_DIR, APP_URL));
        jetty.start();
        Thread.sleep(3000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jetty.stop();
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        httpClient = new HttpClient();
    }

    @Test
    public void testProfList() throws Exception {
        String responseString = executeMethod(PlanService.PROF_PLAN, new String[]{"name", "vorname"}, new String[]{"Fischer", "Max"});
        assertTrue("Lectures expected instead of " + responseString, responseString.contains("\"teacherName\":\"Fischer\",\"teacherVorname\":\"Max\""));
    }
    
    @Test
    public void testForbiddenStates() throws Exception {
        String responseString = executeMethod(PlanService.STD_POSITIONS, new String[]{"id"}, new String[]{"163843"});
        assertTrue("Array of booleans expected, but was: " + responseString, responseString.contains("[0,0,0,0,0,0],"));
    }
    
    private String executeMethod(String service, String[] params, String[] values) throws HttpException, IOException {
        method = new GetMethod(URL + service + "/get");
        NameValuePair[] queries = new NameValuePair[params.length];
        for (int i = 0; i < values.length; i++) {
            queries[i]  = new NameValuePair(params[i], values[i]);
        }
        method.setQueryString(queries);
        httpClient.executeMethod(method);
        return method.getResponseBodyAsString();
    }

}
