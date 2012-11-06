package edu.hm.stundenplan.entities;

import java.io.*;
import java.util.*;

import javax.persistence.*;

@Entity
public class StudyGroup implements Serializable {

    private static final long serialVersionUID = 7228963891938772104L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private DegreeProgram program;
    private int semester;
    private String groupCode;
    @ManyToMany(mappedBy="studyGroup")
    private Set<LectureGroup> lectures = new HashSet<LectureGroup>();
    /**
     * The availability information is intended to be a 35-character string
     * One character per timetable slot. '0' means available. Anything else might receive different semantics in the future.
     */
    private String availability;
    
    public StudyGroup(DegreeProgram program, int semester, String groupCode) {
        this.program = program;
        this.semester = semester;
        this.groupCode = groupCode;
    }

    public StudyGroup() {}
    
    public DegreeProgram getProgram() {
        return program;
    }
    
    public int getSemester() {
        return semester;
    }

    public Integer getId() {
        return id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public Set<LectureGroup> getLectures() {
        return Collections.unmodifiableSet(lectures);
    }

    /**
     * Character-wise encoding: '0' means "available".
     * Semantics: Monday 8:15, Tuesday 8:15 ...... Friday 18:45
     * @return String-encoded availability
     */
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    @Override
    public String toString() {
        return program.getTitle() + "(" + semester + ")";
    }

}
