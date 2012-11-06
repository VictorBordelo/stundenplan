package edu.hm.rest;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

@Path("/json/person")
public class PersonService {
    
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Person[] getProfListAsJSON() {

        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Teacher> allProfs = session.createQuery(
                "from Teacher order by name").list();
        tx.commit();
        return tpPersonArray(allProfs);//.toArray(new Teacher[0]);
    }
    
    private Person[] tpPersonArray(List<Teacher> allProfs) {
        Person[] persons = new Person[allProfs.size()];
        int count = 0;
        for (Teacher teacher : allProfs) {
            persons[count++] = new Person(teacher.getId(), teacher.getVorname(), teacher.getName(), "x@y.de");
        }
        return persons;
    }

    public static class Person {
        private int id;
        private String firstName;
        private String lastName;
        private String email;

        public Person(int id, String firstName, String lastName, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }
    }

}
