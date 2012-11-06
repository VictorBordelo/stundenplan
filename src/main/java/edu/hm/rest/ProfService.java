package edu.hm.rest;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.annotation.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

@Path("/json/profList")
public class ProfService {
    
    /** 
     * Creates a list of teachers that do have lectures assigned!
     * @return Array of teacher objects.
     */
    @GET
    @Path("/get")
    @XmlElementWrapper(name="teachers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getProfListAsJSON() {
        Transaction tx = null;
        try {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Teacher> allProfs = session.createQuery("from Teacher order by name").list();
        Iterator<Teacher> it = allProfs.iterator();
        while(it.hasNext()) {
            Teacher teacher = it.next();
            Criteria criteria = session.createCriteria(Lecture.class);
            criteria.createAlias("teacher", "teacher").
            add(Restrictions.eq("teacher.id", teacher.getId()));
            List<Lecture> thisTeacherslectures = criteria.list();
            if(thisTeacherslectures.size() == 0) {
                it.remove();
            }
        }
        return allProfs;
        }
        finally {
            tx.commit();
        }
    }
    
    @PUT
    @Path("/availability/put/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setAvailability(SimpleAvailabilityDto profDto) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Teacher.class).add(Restrictions.eq("id", profDto.getId()));
        Teacher teacher = (Teacher) criteria.uniqueResult();
        teacher.setAvailability(profDto.getAvailability());
        session.save(teacher);
        tx.commit();
    }
    
    /**
     * This class can be used to produced wrapped json.
     * Just return new ProfList(allProfs) above.
     * @author axel
     *
     */
    private static class ProfList {
        private List<Teacher> teachers = new ArrayList<Teacher>();
        ProfList(List<Teacher> teachers){
            this.teachers = teachers;
            }
        public List<Teacher> getTeachers() {
            return teachers;
        }
    }

}
