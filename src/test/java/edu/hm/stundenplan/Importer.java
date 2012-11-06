package edu.hm.stundenplan;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.dao.*;
import edu.hm.stundenplan.entities.*;

public class Importer {

    private final String filename;

    public Importer(String filename) {
        this.filename = filename;
    }

    public void importFile() throws Exception {
        int counter = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("ISO-8859-1")));
        String line = "";
        int lineCount = 0;
        try {
            while((line = reader.readLine()) != null) {
                lineCount++;
                StringTokenizer stt = new StringTokenizer(line, "\t", true);
                String fwpGroups = getNextToken(stt);
                String groups = getNextToken(stt);
                String teachers = getNextToken(stt);
                Teacher teacherEntity = null;
                if(!"".equals(teachers)) {
                    if(teachers.indexOf(" ") > 0) {
                        teachers = teachers.substring(0, teachers.indexOf(" "));
                        System.out.println(teachers);
                    }
                    final TeacherDao<Teacher> dao = new TeacherDao<Teacher>();
                    teacherEntity = dao.findByName(teachers);
                    if(teacherEntity == null && teachers.indexOf(",") >= 0) {
                        StringTokenizer st = new StringTokenizer(teachers, ",");
                        teacherEntity = dao.findByFullName(st.nextToken(), st.nextToken());
                    }
                }
                String description = getNextToken(stt);

                String module = getNextToken(stt);
                Module moduleEntity = null;
                if(!"".equals(module)) {
                    moduleEntity = (Module) new GenericDao<Module>("name") {}.insert(module, false, module, 4, 5);//defaults for SWS & ECTS
                }
                if(!"".equals(groups)) {
                    handleModule(groups, module);
                }

                String code = getNextToken(stt);

                String type = getNextToken(stt);

                String day = getNextToken(stt);

                String start = getNextToken(stt);

                int duration = Integer.parseInt(getNextToken(stt));

                String rooms = getNextToken(stt);
                String labs = "";
                if(stt.hasMoreElements()) {
                    labs = getNextToken(stt);
                }
                rooms += " " + labs;
                rooms = rooms.trim();

                new LectureDao<Lecture>().insert(moduleEntity, teacherEntity, day, start, duration, groups, rooms);
                counter ++;
            }
        } catch (Exception e) {
            System.err.println("Currently at line " + lineCount + ": "+ line);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleModule(String groups, String module) throws Exception {
        StringTokenizer stt = new StringTokenizer(groups, " ");
        while(stt.hasMoreTokens()) {
            String group = stt.nextToken();
            String degreeProg = group.substring(0, 2);
            int semester = 0;
            if(group.length() > 2) {
                semester= Integer.parseInt(group.substring(2, 3));
            }
            String groupCode = "";
            if(group.length() > 2) {
                groupCode = group.substring(3);
            }
            new GenericDao<DegreeProgram>("title") {}.insert(degreeProg, false, degreeProg);
            new ModuleProgramDao<ModuleProgram>().insert(module, degreeProg, semester);
        }
    }

    public String getNextToken(StringTokenizer stt) {
        String token = stt.nextToken();
        if("\t".equals(token)) {
            return "";
        }
        if(stt.hasMoreTokens()) {
            stt.nextToken();// skip tab
        }
        return  token;
    }

    private void listModules(String program, int semester) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Object result = session.createCriteria(ModuleProgram.class).
        createAlias("program", "program").
        add(Restrictions.eq("program.title", program)).
        add(Restrictions.eq("semester", semester)).
        list();
        System.out.println(result);
        tx.commit();
    }

    private void listTable() {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();

        List<Teacher> result = session.createCriteria(Teacher.class).
            addOrder( Order.asc("name")).
            list();
        for (Teacher teacher : result) {
            System.out.println(teacher);
            List<Lecture> lectures = session.createCriteria(Lecture.class).
                    createAlias("lecture", "lecture").
                    add(Restrictions.eq("teacher.name", teacher.getName())).list();
            Set<String> modules = new HashSet<String>();
            for (Lecture lecture : lectures) {
                String name = lecture.getModule().getName();
                if(modules.add(name)) {
                    System.out.println("   " + name);
                }
            }
        }
            List<Room> rooms = session.createCriteria(Room.class).
            addOrder( Order.asc("name")).
            list();
//        for (Room room : rooms) {
//            System.out.println(room);
//        }
        tx.commit();
    }

    /** Deletes hsql database with all its files.
     * Returns true if all deletions were successful.
     *  If a deletion fails, the method stops attempting to delete and returns false.
     */
    private static boolean deleteDatabase(String databaseName) {
        File file = new File(databaseName + ".log");
        file.delete();
        file = new File(databaseName + ".properties");
        file.delete();
        file = new File(databaseName + ".script");
        file.delete();
        file = new File(databaseName + ".tmp");
        return file.delete();
    }
    
    private static int getNumberOflectures() {
        return new LectureDao<Lecture>().getAllLectures().size();
    }
    
    public static void main(String[] args) throws Exception {
        Importer importer = new Importer("test-data/stdplan-ws12");

        ProductionSessionFactory.getDefault().setDatabase(BaseTestData.DATABASE);
        PersonImporter personImporter = new PersonImporter("test-data/persons.xml");//"https://fi.cs.hm.edu/fi/rest/public/person/prof.xml");
        personImporter.importPersons();
        RoomImporter roomImporter= new RoomImporter("test-data/raeume.txt");
        roomImporter.importRooms();
       
        GroupImporter groupImporter = new GroupImporter();
        groupImporter.importGroups();
        importer.importFile();
        System.out.println(getNumberOflectures() + " lectures found.");
    }

}
