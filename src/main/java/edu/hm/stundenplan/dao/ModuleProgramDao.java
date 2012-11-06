package edu.hm.stundenplan.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

public class ModuleProgramDao<T> extends GenericDao<ModuleProgram> {

    public void insert(String module, String degreeProg, int semester) {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Module moduleEntity = (Module) session.createCriteria(Module.class).
        add(Restrictions.eq("name", module)).uniqueResult();
        DegreeProgram degProgEntity = (DegreeProgram) session.createCriteria(DegreeProgram.class).
        add(Restrictions.eq("title", degreeProg)).uniqueResult();
        
        Object entry = session.createCriteria(persistentClass).
        add(Restrictions.eq("module", moduleEntity)).
        add(Restrictions.eq("program", degProgEntity)).//semester irrelevant here (FWPs?)
            uniqueResult();
        if(entry == null) {
            ModuleProgram modProg = new ModuleProgram(moduleEntity, degProgEntity, semester);
            session.save(modProg);
        }
        tx.commit();
    }

}
