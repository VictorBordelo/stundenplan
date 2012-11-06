package edu.hm.rest;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

@Path("/json/plan")
public class PlanService {

    public static final String PROF_PLAN = "/profplan";
    public static final String STUDY_GROUP_PLAN = "/studygroupPlan";
    public static final String ROOM_PLAN = "/roomPlan";
    public static final String STD_POSITIONS = "/stundenPositions";
    public static final String DETAILED_STD_POSITIONS = "/detailedStundenPositions";
    private static final int DAYS_PER_WEEK = 5;
    private static final int SLOTS_PER_DAY = 7;
    
    public static final int TEACHER_UNAVAILABLE = 1;
    public static final int GROUP_UNAVAILABLE = 2;
    public static final int ROOM_UNAVAILABLE = 4;
    
    @GET
    @Path(PROF_PLAN + "/get")
    @Produces(MediaType.APPLICATION_JSON)
    public LectureDto[] getProfPlanAsJSON(@QueryParam("id")int teacherId) {
        return createProxiesFromlectures(getProfAllocations(teacherId));
    }
    
    private Set<Lecture> getProfAllocations(int teacherId) {
        Transaction tx = null;
        try {
            Session session = ProductionSessionFactory.getDefault().getCurrentSession();
            tx = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<Lecture> lectures = session.createCriteria(Lecture.class).
            createAlias("teacher", "teacher").
            add(Restrictions.eq("teacher.id", teacherId)).list();
            return new HashSet<Lecture>(lectures);
        } finally {
            tx.commit();
        }
    }
    
    /**
     * Returns an array which contains all LectureDto's which represent for a
     * specified study group. All lectures which are visited by the specified study group
     * will be returned.
     * 
     * @author	Benedikt Zönnchen
     * @param 	studyGroupId identifier for the specified study group
     * @return	an array which contains all LectureDto's (all lectures for the specified study group)
     */
    @GET
    @Path(STUDY_GROUP_PLAN + "/get")
    @Produces(MediaType.APPLICATION_JSON)
    public LectureDto[] getStudyGroupPlanAsJSON(@QueryParam("id")int studyGroupId) {
        return createProxiesFromlectures(getGroupAllocations(studyGroupId));
    }

    /**
     * @return all lectures where a given group is involved.
     */
    public Set<Lecture> getGroupAllocations(int studyGroupId) {
        Transaction tx = null;
        try {
            Session session = ProductionSessionFactory.getDefault().getCurrentSession();
            tx = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<LectureGroup> lectureGroups = session.createCriteria(LectureGroup.class).
            add(Restrictions.eq("studyGroup.id", studyGroupId)).list();
            Set<Lecture> lectures = new HashSet<Lecture>();
            for(LectureGroup lectureGroup : lectureGroups)
            {
                lectures.add(lectureGroup.getLecture());
            }
            return lectures;
        } finally {
            tx.commit();
        }
    }
    
    /**
     * Returns an array which contains all LectureDto's which represent a lecture in the
     * specified room. All lectures which use the specified room will be returned.
     * 
     * @author	Benedikt Zönnchen
     * @param 	roomId identifier for the specified room
     * @return	an array which contains all LectureDto's (all lectures of this room)
     */
    @GET
    @Path(ROOM_PLAN + "/get")
    @Produces(MediaType.APPLICATION_JSON)
    public LectureDto[] getRoomPlanAsJSON(@QueryParam("id")int roomId) {
        return createProxiesFromlectures(getRoomAllocations(roomId));
    }
    
    private Set<Lecture> getRoomAllocations(int roomId) {
        Transaction tx = null;
        try {
            Session session = ProductionSessionFactory.getDefault().getCurrentSession();
            tx = session.beginTransaction();
            @SuppressWarnings("unchecked")
            List<Lecture> allLectures = session.createCriteria(Lecture.class).list();
            Set<Lecture> lectures = new HashSet<Lecture>();
            for(Lecture lecture : allLectures)
            {
                for(Room room : lecture.getRooms())
                {
                    if(room.getId() == roomId)
                    {
                        lectures.add(lecture);
                        break;
                    }
                }
            }
            return lectures;
        } finally {
           tx.commit();
        }
    }

    @PUT
    @Path(STD_POSITIONS + "/put/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changePos(LectureDto lectureDto) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Lecture lecture = (Lecture) session.load(Lecture.class, lectureDto.id);
        for(TimeSlot slot: TimeSlot.values()) {
            if(slot.ordinal() == lectureDto.getZeit()) {
                lecture.setStart(slot);
                break;
            }
            lecture.setStart(TimeSlot.NONE);
        }
        for(WeekDay day : WeekDay.values()) {
            if(day.ordinal() == lectureDto.getTag()) {
                lecture.setDay(day);
                break;
            }
            lecture.setDay(WeekDay.NONE);
        }
        session.save(lecture);
        tx.commit();
    }
    
    /**
     * Returns an array which represent the information why a lecture can not be placed 
     * to a day and time. The array contains numbers which represents the reasons.
     * 1 (001) => teacher, 2 (010) => lecture group, 4 (100) => room. The reasons
     * can be combined, for example 6 (110) means room+teacher and so on.
     * 
     * @author 	Benedikt Zönnchen
     * @param 	lectureId the lecture id
     * @return 	an array which the information why a lecture can not be placed to a day and time
     */
    @GET
    @Path(DETAILED_STD_POSITIONS + "/get")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public int[][] getDetailedRestrictions(@QueryParam("id")int lectureId) {
        int[][] restrictions = new int[SLOTS_PER_DAY + 1][DAYS_PER_WEEK + 1];
        
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Lecture lecture = (Lecture) session.load(Lecture.class, lectureId);
        Teacher teacher = lecture.getTeacher();
        Set<LectureGroup> groups = lecture.getLectureGroupMappings();
        Set<Integer>groupIds = new HashSet<Integer>();
        for (Iterator<LectureGroup> iterator = groups.iterator(); iterator.hasNext();) {
            LectureGroup lectureGroup = (LectureGroup) iterator.next();
            if(lectureGroup.getStudyGroup() != null) {
                groupIds.add(lectureGroup.getStudyGroup().getId());
            }
        }
        tx.commit();
        
        if(teacher != null) {
            setTeacherRestrictions(teacher, restrictions);
        }
        setGroupRestrictions(groupIds, restrictions);
        
        setRoomRestriction(lecture.getRoomRequirements(), lecture.getNumberOfParticipants(), restrictions);
        
        return restrictions;
    }
    
    private void setRoomRestriction(Set<RoomRequirement> roomRequirements, int size, 
            int[][] restrictions) {
        int[][] availability = new int[SLOTS_PER_DAY + 1][DAYS_PER_WEEK + 1];
        int av = SLOTS_PER_DAY * DAYS_PER_WEEK;
        // start: all slots unavailable
        for (int i = 1; i < availability.length; i++) {
            for (int j = 1; j < availability[i].length; j++) {
                availability[i][j] = ROOM_UNAVAILABLE;
            }
        }
        for (Iterator iterator = roomRequirements.iterator(); iterator
                .hasNext();) {
            RoomRequirement roomRequirement = (RoomRequirement) iterator.next();
            // Assumption: requirenNumber === 1 !! 
            RoomType type = roomRequirement.getRequirementType();
            List<Room> possibleRooms = type.getRooms();
            // gets extreme slowly for "realistic" scenario
            rooms:
            for (Room room : possibleRooms) {
                if(size > room.getSize()) {
                    continue;
                }
                int[][] occupancy = new int[SLOTS_PER_DAY + 1][DAYS_PER_WEEK + 1];
                Set<Lecture> lectures = getRoomAllocations(room.getId());
                fillRestrictions(lectures, occupancy, ROOM_UNAVAILABLE);
                for (int i = 1; i < occupancy.length; i++) {
                    for (int j = 1; j < occupancy[i].length; j++) {
                        if(occupancy[i][j] == 0 && availability[i][j] != 0) {
                            availability[i][j] = 0;
                            av--;
                            if(av == 0) {
                                break rooms;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < availability.length; i++) {
            for (int j = 0; j < availability[i].length; j++) {
                restrictions[i][j] |= availability[i][j];
            }
        }
    }

    private void setTeacherRestrictions(Teacher teacher, int[][] restrictions) {
        String availability = teacher.getAvailability();
        for(int slot = 0; slot < SLOTS_PER_DAY; slot++) {
            for(int day = 0; day < DAYS_PER_WEEK; day++) {
                if(availability.charAt((slot * DAYS_PER_WEEK) + day) != '0') {
                    restrictions[slot + 1][day + 1] |= TEACHER_UNAVAILABLE;
                }
            }
        }
        Set<Lecture> lectures = getProfAllocations(teacher.getId());
        fillRestrictions(lectures, restrictions, TEACHER_UNAVAILABLE);
    }
    
    private void setGroupRestrictions(Set<Integer> groupIds, int[][] restrictions) {
        for (Iterator<Integer>iterator = groupIds.iterator(); iterator.hasNext();) {
            int groupId = (Integer)iterator.next();
            Set<Lecture> lectures = getGroupAllocations(groupId);
            fillRestrictions(lectures, restrictions, GROUP_UNAVAILABLE);
        }
    }
    
    private void setRoomRestrictions(Lecture lecture, int[][] restrictions) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        
        session.update(lecture);
        Set<RoomRequirement> rr = lecture.getRoomRequirements();
        for (Iterator iterator = rr.iterator(); iterator.hasNext();) {
            RoomRequirement roomRequirement = (RoomRequirement) iterator.next();
            RoomType type = roomRequirement.getRequirementType();
            int number = roomRequirement.getRequiredNumber();
            
        }
        tx.commit();
    }
    
    private void fillRestrictions(Set<Lecture> lectures, int[][] restrictions, int unavailableResource) {
        for (Iterator<Lecture> iterator = lectures.iterator(); iterator.hasNext();) {
            Lecture lecture = (Lecture) iterator.next();
            restrictions[lecture.getSlot().ordinal()][lecture.getDay().ordinal()] |= unavailableResource;
        }
    }
    
    private LectureDto[] createProxiesFromlectures(Collection<Lecture> lectures) {
        List<LectureDto> proxies = new ArrayList<LectureDto>();
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        for (Lecture lecture : lectures) {
            session.update(lecture);
            String rooms = "";
            Set<Room> roomSet = lecture.getRooms();
            int[] roomIds = new int[roomSet.size()];
            int index = 0;
            for (Room room : roomSet) {
                rooms += room.getName() + " ";
                roomIds[index++] = room.getId();
            }
            rooms = rooms.trim();
            LectureDto lectureDto = new LectureDto(lecture.getName(), lecture.getId(), 
            		lecture.getDay(), lecture.getSlot().ordinal(), rooms, roomIds);
            if(lecture.getTeacher() != null)
            {
            	lectureDto.setTeacherName(lecture.getTeacher().getName());
            	lectureDto.setTeacherVorname(lecture.getTeacher().getVorname());
            }
            String[] studyGroupNames = new String[lecture.getLectureGroupMappings().size()];
            int[] studyGroupIds = new int[studyGroupNames.length];
            
            index = 0;
            for(LectureGroup lectureGroup : lecture.getLectureGroupMappings())
            {
            	StudyGroup studyGroup = lectureGroup.getStudyGroup();
            	if(studyGroup != null)
            	{
            		//TODO groupCode bugged: Länge ist 255 obwohl nur 1 Zeichen darum .trim(), allerdings fragwürdig warum der code 255 zeichen hat.
            		studyGroupNames[index] = (studyGroup.getProgram().getTitle()+studyGroup.getSemester()+studyGroup.getGroupCode().trim());
            		studyGroupIds[index] = studyGroup.getId();
            		index++;
            	}
            }
            lectureDto.setStudyGroupIds(studyGroupIds);
            lectureDto.setStudyGroupNames(studyGroupNames);
            proxies.add(lectureDto);
        }
        tx.commit();
        return proxies.toArray(new LectureDto[0]);
    }
    
    @SuppressWarnings("unused")
    private static class LectureDto {
        private String title;
        private int id;
        private int tag;
        private int zeit;
        private String content;
        
        /** the name of the teacher of this lecture representation. */
        private String teacherName = "";
        
        /** the first name of the teacher of this lecture representation. */ 
        private String teacherVorname = "";
        
        /** an array of all room id's of this lecture representation. */
        private int[] roomIds;
        
        /** an array of all study group id's of this lecture representation. */
        private int[] studyGroupIds;
        
        /** an array of all study group names of this lecture representation. */
        private String[] studyGroupNames;
        
        public LectureDto(final String name, int id, final WeekDay weekDay, final int zeit, final String content, final int[] roomIds)
        {
            this.title = name;
            this.id = id;
            this.tag = weekDay.ordinal();
            this.zeit = zeit;
            this.content = content;
            this.roomIds = roomIds;
        }
        
        /** default constructor */
        public LectureDto() {}

        
        
        /**
         * returns an array with all room id's of this lecture representation.
         * 
         * @author Benedikt Zönnchen
		 * @return the room id's of this lecture representation
		 */
		public int[] getRoomIds()
		{
			return roomIds;
		}

		/**
		 * set the room id's of the lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @param roomIds the room id's to set for this lecture representation
		 */
		public void setRoomIds(int[] roomIds)
		{
			this.roomIds = roomIds;
		}

		/**
		 * returns an array with all study group id's of this lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @return an array with all study group id's
		 */
		public int[] getStudyGroupIds()
		{
			return studyGroupIds;
		}

		/**
		 * set the study group id's of the lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @param studyGroupIds the study group id's to set
		 */
		public void setStudyGroupIds(int[] studyGroupIds)
		{
			this.studyGroupIds = studyGroupIds;
		}

		/**
		 * @return the studyGroupNames
		 */
		public String[] getStudyGroupNames()
		{
			return studyGroupNames;
		}

		/**
		 * @param studyGroupNames the studyGroupNames to set
		 */
		public void setStudyGroupNames(String[] studyGroupNames)
		{
			this.studyGroupNames = studyGroupNames;
		}

		/**
		 * returns the teacher name of this lecture representation.
		 * 
         * @author Benedikt Zönnchen
		 * @return the teacher name
		 */
		public String getTeacherName()
		{
			return teacherName;
		}

		/**
		 * set the teacher name of this lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @param teacherName the teacherName to set
		 */
		public void setTeacherName(String teacherName)
		{
			this.teacherName = teacherName;
		}

		/**
		 * returns the teacher first name of this lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @return the teacher first name
		 */
		public String getTeacherVorname()
		{
			return teacherVorname;
		}

		/**
		 * set the teacher first name of this lecture representation.
		 * 
		 * @author Benedikt Zönnchen
		 * @param teacherVorname the first name to set
		 */
		public void setTeacherVorname(String teacherVorname)
		{
			this.teacherVorname = teacherVorname;
		}

		public void setTitle(String title) {
            this.title = title;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public void setZeit(int zeit) {
            this.zeit = zeit;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public int getZeit() {
            return zeit;
        }

        public String getContent() {
            return content;
        }

        public int getId() {
            return id;
        }
        
        public int getTag() {
            return tag;
        }
        
        @Override
        public String toString() {
            return title + " " + id + " " + tag + " " + zeit + " " + content;
        }
        
    }

}
