package edu.hm;

import javax.servlet.*;

import org.apache.log4j.*;

import edu.hm.stundenplan.*;


public class HibernateListener implements ServletContextListener {

    public static final String DATABASE = "jdbc:hsqldb:hsql://localhost/plandb";

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        ProductionSessionFactory.getDefault().close();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String database = DATABASE;
        Logger.getLogger(this.getClass()).info("Booting database " + database);
        System.out.println("Booting database " + database);
        ProductionSessionFactory.getDefault().setDatabase(database);
    }

}
