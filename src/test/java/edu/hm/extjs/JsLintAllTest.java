package edu.hm.extjs;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.*;

import com.googlecode.jslint4java.*;

@RunWith(Parameterized.class)
public class JsLintAllTest {

    @Parameters public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"src/main/webapp/ext/MyViewport.js"},
                {"src/main/webapp/ext/MyViewport.ui.js"},
                {"src/main/webapp/ext/ForbiddenStateStore.js"},
                {"src/main/webapp/ext/GruppenStore.js"},
                {"src/main/webapp/ext/ProfStore.js"},
                {"src/main/webapp/ext/RaumStore.js"},
                {"src/main/webapp/ext/stundenplan.js"},
        });
    }

    private String fileName;
    
    public JsLintAllTest(String fileName) {
        this.fileName = fileName;
    }
    
    @Test
    @Ignore
    public void testCodeQuality() throws IOException, SAXException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        InputStreamReader bis = new InputStreamReader(fis);
        BufferedReader dis = new BufferedReader(bis);
        JSLintBuilder  builder = new JSLintBuilder();
        JSLint jsLint = builder.fromDefault();
        JSLintResult lint = jsLint.lint(fileName, dis);
        List<Issue> issues = lint.getIssues();
        StringBuilder issueString = new StringBuilder();
        if(!issues.isEmpty()) {
            for (Issue issue : issues) {
                issueString.append(issue.toString());
            }
        }
        assertEquals("No JSLint issues expected but got\n" + issueString.toString(), 0, issues.size());
    }

}
