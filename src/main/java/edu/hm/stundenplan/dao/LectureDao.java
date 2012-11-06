package edu.hm.stundenplan.dao;

import java.util.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

public class LectureDao<T> extends GenericDao<Lecture> {

    private Transaction tx;
    private Criteria criteria;
    private Session session;

    public Lecture insert(Module module, Teacher teacher, String day, String slot,
            int duration, String groups, String rooms) {
        Lecture lecture;
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        lecture = new Lecture(module, teacher);
        lecture.setDay(WeekDay.valueOf(day));
        lecture.setStart(timeToSlot(slot));
        lecture.setDuration(duration);
        handleRooms(lecture, rooms);
        session.save(lecture);
        handleGroups(lecture, groups);
        tx.commit();
        return lecture;
    }

    private TimeSlot timeToSlot(String start) {
        if("8:15".equals(start)) {
            return TimeSlot.Slot1;
        }
        if("10:00".equals(start)) {
            return TimeSlot.Slot2;
        }
        if("11:45".equals(start)) {
            return TimeSlot.Slot3;
        }
        if("13:30".equals(start)) {
            return TimeSlot.Slot4;
        }
        if("15:15".equals(start)) {
            return TimeSlot.Slot5;
        }
        if("17:00".equals(start)) {
            return TimeSlot.Slot6;
        }
        if("18:45".equals(start)) {
            return TimeSlot.Slot7;
        }
        return TimeSlot.NONE;
    }

    private void handleRooms(Lecture lecture, String rooms) {
        StringTokenizer stt = new StringTokenizer(rooms, " ");
        while(stt.hasMoreTokens()) {
            String room = stt.nextToken();
            Room roomEntity = (Room) session.createCriteria(Room.class).
                    add(Restrictions.eq("name", room)).uniqueResult();
            lecture.addRoom(roomEntity);
            List<RoomType> types = session.createCriteria(RoomType.class).createAlias("rooms", "rooms").add(Restrictions.eq("rooms.name", roomEntity.getName())).list();
            for (RoomType roomType : types) {
                if(roomEntity.getName().equals(roomType.getName())) {
//                if("Hörsaal".equals(roomType.getName()) || "Labor".equals(roomType.getName()) || "Seminarraum".equals(roomType.getName())) { // change lines for more generic solution :-)
                    RoomRequirement rr = new RoomRequirement(roomType, 1);
                    session.save(rr);
                    lecture.addRoomRequirement(rr);
                    lecture.setNumberOfParticipants(roomEntity.getSize());
                    break;
                }
            }
        }
    }

    private void handleGroups(Lecture lecture, String groups) {
        StringTokenizer stt = new StringTokenizer(groups, " ");
        Set<String> groupsAsString = new HashSet<String>();//to check if we already had that group
        while(stt.hasMoreTokens()) {
            String group = stt.nextToken();
            if(groupsAsString.add(group)) {
                String degreeProg = group.substring(0, 2);
                int semester = 0;
                String groupCode = "";
                if(group.length() > 2) {
                    semester = Integer.parseInt(group.substring(2, 3));
                    groupCode = group.substring(3);
                }
                StudyGroup groupEntity = (StudyGroup) session.createCriteria(StudyGroup.class).
                        createAlias("program", "program").
                        add(Restrictions.eq("program.title", degreeProg)).
                        add(Restrictions.eq("semester", semester)).
                        add(Restrictions.eq("groupCode", groupCode)).
                        uniqueResult();
                if(groupEntity != null) {
                    session.save(new LectureGroup(lecture, groupEntity, "" + ((groupCode.length() > 0) ? groupCode.charAt(0) : "")));
                }
            }
        }
    }

    public Set<Lecture> findInstanceByCriteria(String searchString) {
        List<Lecture> lectures;
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        try {
            criteria = session.createCriteria(persistentClass).
                    createAlias("module", "module").
                    createAlias("teacher", "teacher").
                    add(Restrictions.or(Restrictions.ilike("module.name", searchString, MatchMode.START), 
                            Restrictions.ilike("teacher.name", searchString, MatchMode.START)));
            lectures = criteria.list();
        } finally {
            tx.commit();
        }
        return new HashSet<Lecture>(lectures);
    }

    public Set<Lecture> findByCriteria(String searchString) {
        List<Lecture> lectures;
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        criteria = session.createCriteria(persistentClass).
                createAlias("teacher", "teacher").
                createAlias("module", "module").
                add(Restrictions.or(Restrictions.ilike("module.name", searchString, MatchMode.START), 
                        Restrictions.ilike("teacher.name", searchString, MatchMode.START)));
        lectures = criteria.list();
        tx.commit();
        return new HashSet<Lecture>(lectures);
    }

    public Set<Lecture> findByTeacher(Teacher teacher) {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Lecture> lectures = session.createCriteria(Lecture.class).
        createAlias("teacher", "teacher").
        add(Restrictions.and(Restrictions.eq("teacher.vorname", teacher.getVorname()), 
                Restrictions.eq("teacher.name", teacher.getName()))).list();
        tx.commit();
        return new HashSet<Lecture>(lectures);
    }

    public Set<Lecture> getAllLectures() {
        session = ProductionSessionFactory.getDefault().getCurrentSession();
        tx = session.beginTransaction();
        List<Lecture> lectures = session.createCriteria(Lecture.class).list();
        tx.commit();
        return new HashSet<Lecture>(lectures);
    }

}
