package edu.hm.stundenplan;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import edu.hm.stundenplan.dao.*;
import edu.hm.stundenplan.entities.*;

public class PersonImporter {
    
    public static final String DEFAULT_AVAILABILITY = "00000000000000000000000000000000000";
    private final String url;

    PersonImporter(String url) throws MalformedURLException {
        this.url = url;
    }
    
    public void importPersons() throws Exception {
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(url));
        NodeList persons = xmlDocument.getElementsByTagName("person");
        Random rand = new Random();
        for(int personNr = 0; personNr < persons.getLength(); personNr++) {
            String firstName = null, lastName = null, sex = null;
            TeacherStatus status = null;
            Node singlePerson = persons.item(personNr);
            NodeList personData = singlePerson.getChildNodes();
            for(int item = 0; item < personData.getLength(); item++) {
                Node data = personData.item(item);
                if(data.getNodeName().equals("firstname")) {
                    firstName = data.getFirstChild().getNodeValue();
                }
                if(data.getNodeName().equals("lastname")) {
                    lastName = data.getFirstChild().getNodeValue();
                }
                if(data.getNodeName().equals("sex")) {
                    sex = data.getFirstChild().getNodeValue();
                }
                if(data.getNodeName().equals("status")) {
                    status = TeacherStatus.valueOf(data.getFirstChild().getNodeValue());
                }
            }
            if(firstName != null && (status == TeacherStatus.fellow || status == TeacherStatus.prof || status == TeacherStatus.lba)) {
                final TeacherDao<Teacher> dao = new TeacherDao<Teacher>();
                if(dao.findByFullName(lastName, firstName) == null) {
                    Teacher teacher = new Teacher(lastName, firstName, sex, status);
                    teacher.setAvailability(DEFAULT_AVAILABILITY);
                    dao.insert(teacher);
                }
            }
        }
    }
}
