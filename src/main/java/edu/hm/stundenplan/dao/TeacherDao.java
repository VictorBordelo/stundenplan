package edu.hm.stundenplan.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

public class TeacherDao<T> extends GenericDao<Teacher> {

    public Teacher findByName(String name) {
        Criteria criteria;
        Transaction tx;
        Teacher teacher;
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        try {
            criteria = session.createCriteria(persistentClass).
                    add(Restrictions.eq("name", name));
            teacher = (Teacher) criteria.uniqueResult();
        } finally {
            tx.commit();
        }
        return teacher;

    }
    
    public Teacher findByFullName(String name, String firstName) {
        Criteria criteria;
        Transaction tx;
        Teacher teacher;
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        try {
            criteria = session.createCriteria(persistentClass).
                    add(Restrictions.and(
                            Restrictions.eq("name", name),
                            Restrictions.eq("vorname", firstName)
                            ));
            teacher = (Teacher) criteria.uniqueResult();
        } finally {
            tx.commit();
        }
        return teacher;

    }
    
}
