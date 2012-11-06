package edu.hm.stundenplan;

import java.io.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;


public abstract class BaseTestClass extends JdbcBasedDBTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ProductionSessionFactory.getDefault().setDatabase(BaseTestData.DATABASE);
    }

    @Override
    protected String getConnectionUrl() {
        return BaseTestData.DATABASE;
    }

    @Override
    protected String getDriverClass() {
        return BaseTestData.DRIVER;
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
            return DatabaseOperation.CLEAN_INSERT;
    }

    @Override
    protected IDataSet getDataSet() throws Exception
    {
        return new FlatXmlDataSet(new FileInputStream(BaseTestData.TEST_DATA_BASE_XML));
    }

}
