package edu.hm.stundenplan.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

public class StudyGroupDao<T> extends GenericDao<StudyGroup> {

    public void insert(String degreeProg, int semester, String groupCode) {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();

        DegreeProgram degProgEntity = (DegreeProgram) session.createCriteria(DegreeProgram.class).
        add(Restrictions.eq("title", degreeProg)).uniqueResult();
        
        Object entry = session.createCriteria(persistentClass).
            add(Restrictions.eq("program", degProgEntity)).
            add(Restrictions.eq("semester", semester)).
            add(Restrictions.eq("groupCode", groupCode)).
            uniqueResult();
        if(entry == null) {
            StudyGroup group = new StudyGroup(degProgEntity, semester, groupCode);
            session.save(group);
        }
        tx.commit();
    }
}
