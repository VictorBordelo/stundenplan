package edu.hm.stundenplan.dao;

import java.lang.reflect.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;

/**
 * must be abstract so we can derive T in an implementation!
 */
public abstract class GenericDao<T> {

    protected final Class<T> persistentClass;
    protected final String queryIdentifier;
    protected Session session;


    public GenericDao(String identifier) {
        this.queryIdentifier = identifier;
        final Class<? extends GenericDao> myClass = this.getClass();
        Type genericSuperclass = myClass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
        persistentClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }
    
    public GenericDao() {
        this("");
    }
    
    public void insert(T t) {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(t);
        tx.commit();
    }

    /**
     * @param entities blank separated list of T-entities
     * @param separate  true to separate into single entities
     * @return 
     * @throws Exception 
     */
    public Object insert(String entities, boolean separate, Object... parameters) throws Exception {
        StringTokenizer stt = new StringTokenizer(entities, " ");
        if(separate) {
            Object entity = null;
            while(stt.hasMoreTokens()) {
                String singleEntity = stt.nextToken();
                entity = insertEntity(singleEntity, singleEntity);
            }
            return entity;
        }
        else {
            return insertEntity(entities, parameters);
        }
    }

    private Object insertEntity(String entityName, Object... parameters) throws Exception {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Object entry = session.createCriteria(persistentClass).add(Restrictions.eq(queryIdentifier, entityName)).uniqueResult();
        if(entry == null) {
            entry = createObject(entityName, parameters);
            session.save(entry);
        }
        tx.commit();
        return entry;
    }
    
    private T createObject(String id, Object... parameters) throws Exception {
        List<Class> types = new ArrayList<Class>();
        for (Object parameter : parameters) {
            types.add(parameter.getClass());
        }
        Constructor<T> ctor = persistentClass.getDeclaredConstructor(types.toArray(new Class[0]));
        if(parameters.length == 0) {
            return ctor.newInstance(id);
        }
        return ctor.newInstance(parameters);
    }
    
}
