package edu.hm.rest;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;
import org.junit.*;
import org.xml.sax.*;

import com.googlecode.jslint4java.*;

public class ProfServiceTest extends BasicRestTest {

    private static final String URL = "http://localhost:" + PORT + APP_URL + "/rest/json/profList/get";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jetty = new Server(PORT);
        jetty.setHandler(new WebAppContext(WEBAPP_DIR, APP_URL));
        jetty.start();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jetty.stop();
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        HttpClient httpClient = new HttpClient();
        GetMethod get = new GetMethod(URL);
        httpClient.executeMethod(get);
        responseString = get.getResponseBodyAsString();
        System.out.println(responseString);
    }

    @Test
    public void testProfList() throws IOException, SAXException {
        JSLintBuilder  builder = new JSLintBuilder();
        JSLint jsLint = builder.fromDefault();
        JSLintResult lint = jsLint.lint(URL, responseString);
//        List<Issue> issues = lint.getIssues();
//        assertEquals("No JSLint issues expected in " + responseString, 0, issues.size());
        assertTrue("We expect json here", lint.isJson());
        assertTrue("name expected", responseString.contains("{\"name\":\"Fischer\","));
    }

}
