package edu.hm.stundenplan.dao;


import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

public class LectureDaoTest {
    
    private static final String SEARCH_STRING = "Fi";
    private LectureDao<Lecture> lectureDao;
    
    @Before
    public void setUp() throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("java.class.path"));
        ProductionSessionFactory.getDefault().setDatabase(BaseTestData.DATABASE);
        lectureDao = new LectureDao<Lecture>();
    }

    @Test
    public void testFindByCriteria() {
        Set<Lecture> listCriteria = lectureDao.findByCriteria(SEARCH_STRING);
        assertTrue(listCriteria.size() > 0);
    }

    @Test
    public void findFischerMax() {
        TeacherDao<Teacher> teacherDao = new TeacherDao<Teacher>();
        Teacher teacher = teacherDao.findByFullName("Fischer", "Max");
        Set<Lecture> lectures = lectureDao.findByTeacher(teacher);
        assertEquals(10, lectures.size());
    }
    
    @Test
    public void testAllLectures() {
        Set<Lecture> allLectures = lectureDao.getAllLectures();
        assertEquals(338, allLectures.size());
    }
}
