package edu.hm.rest;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.eclipse.jetty.server.*;
import org.junit.*;

import edu.hm.stundenplan.*;

@Ignore
public class BasicRestTest {

    protected static final String APP_URL = "";
    protected static final String WEBAPP_DIR = "./src/main/webapp/";
    protected static final int PORT = 8089;
    protected static Server jetty;
    protected GetMethod method;
//  protected PostMethod method;
    protected HttpClient httpClient = null;
    protected String responseString;
    
    @Before
    public void setUp() throws Exception {
        ProductionSessionFactory.getDefault().setDatabase(BaseTestData.DATABASE);
    }

}
