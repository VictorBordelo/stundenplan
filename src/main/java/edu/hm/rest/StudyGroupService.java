package edu.hm.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.stundenplan.ProductionSessionFactory;
import edu.hm.stundenplan.entities.*;

/**
 * The rest service for the studyGroup issues.
 * 
 * @author Benedikt Zönnchen
 */
@Path("/json/studygroup")
public class StudyGroupService
{
	/**
	 * Return a list containing all study-groups 
	 * (semester, group-code (A,B,...), course (IF, IC, ...)).
	 * 
	 * @author Benedikt Zönnchen
	 * @return returns a list containing all study-groups
	 */
	@GET
	@Path("/studygrouplist/get")
	@Produces(MediaType.APPLICATION_JSON)
	public StudyGroupDto[] getStudyGroupListAsJSON()
	{
		Session session = ProductionSessionFactory.getDefault()
				.getCurrentSession();
		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<StudyGroup> studyGrps = session.createQuery(
                "from StudyGroup ").list();
		tx.commit();
		return createStudyGroupDtoList(studyGrps);
	}

	// TODO generify over class Teacher, Room and Group
    @PUT
    @Path("/availability/put/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setAvailability(SimpleAvailabilityDto groupDto) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(StudyGroup.class).add(Restrictions.eq("id", groupDto.getId()));
        StudyGroup group = (StudyGroup) criteria.uniqueResult();
        group.setAvailability(groupDto.getAvailability());
        session.save(group);
        tx.commit();
    }
    
	/**
	 * parse a list of studyGroups into a array of studyGroupDtos. This is necessary for parsing into json.
	 * 
	 * @author Benedikt Zönnchen
	 * @param studyGroups a list of studyGroup objects
	 * @return an array of studyGroupDto objects
	 */
	private StudyGroupDto[] createStudyGroupDtoList(List<StudyGroup> studyGroups)
	{
		List<StudyGroupDto> result = new ArrayList<StudyGroupDto>();
		for(StudyGroup studyGroup : studyGroups)
		{
			//TODO: Bug: studyGroup.getGroupCode() liefert immer einen String der Länge 255 obwohl es nur ein Zeichen sein sollte.
			result.add(new StudyGroupDto(studyGroup.getId(), studyGroup.getSemester(), studyGroup.getProgram().getTitle(), studyGroup.getGroupCode().trim()));
		}
		return result.toArray(new StudyGroupDto[0]);
	}
	
	/**
	 * A simple wrapper class which contain only the necessary informations of a study group.
	 * It is a representation of the real study group object.
	 * 
	 * @author Benedikt Zönnchen
	 */
	private static class StudyGroupDto
	{
		private int id;
		private int semester;
		private String program;
		private String groupCode;

		private StudyGroupDto(final int id, final int semester, final String program, final String groupCode)
		{
			this.id = id;
			this.semester = semester;
			this.program = program;
			this.groupCode = groupCode;
		}

		/**
		 * @return the id
		 */
		public int getId()
		{
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(int id)
		{
			this.id = id;
		}

		/**
		 * @return the groupCode
		 */
		public String getGroupCode()
		{
			return groupCode;
		}

		/**
		 * @param groupCode the groupCode to set
		 */
		public void setGroupCode(String groupCode)
		{
			this.groupCode = groupCode;
		}

		/**
		 * @return the semester
		 */
		public int getSemester()
		{
			return semester;
		}

		/**
		 * @param semester the semester to set
		 */
		public void setSemester(int semester)
		{
			this.semester = semester;
		}

		@Override
		public String toString()
		{
			return this.getProgram() + this.getSemester() + this.getGroupCode();
		}
		
		/**
		 * @return the prgramm
		 */
		public String getProgram()
		{
			return program;
		}

		/**
		 * @param prgramm the prgramm to set
		 */
		public void setProgram(String programm)
		{
			this.program = programm;
		}
	}
}
