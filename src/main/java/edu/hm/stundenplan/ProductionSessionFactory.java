package edu.hm.stundenplan;

import java.util.*;

import org.apache.log4j.*;
import org.hibernate.*;
import org.hibernate.cfg.*;

public class ProductionSessionFactory { 

    private static final ProductionSessionFactory theInstance = new ProductionSessionFactory();
    private SessionFactory sessionFactory;
    private AnnotationConfiguration cfg;
    private final Map<String, SessionFactory> sessionFactoryMap = new HashMap<String, SessionFactory>();

    /**
     * Default constructor. It is private to guaranty singleton
     * configure Hibernate from hibernate.cfg.xml. This is expected to be in
     * the class path = "src folder" = "root folder of classes"
     */
    private ProductionSessionFactory() {
    }

    public static ProductionSessionFactory getDefault() {
        return theInstance;
    }

    public void setDatabase(String databaseUrl) {
        System.out.println("Setting database: " + databaseUrl);
        Logger.getLogger(this.getClass()).info("Setting database: " + databaseUrl);
        cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.connection.url", databaseUrl).configure("/hibernate.cfg.xml");
        sessionFactory = cfg.buildSessionFactory();
        sessionFactoryMap.put(databaseUrl, sessionFactory);
    }

    /**
     * Returns the current session of the current session factory.
     * @return
     */
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Closes currently used factory.
     */
    public void close() {
        sessionFactory.close();
    }

}
