package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;

@Entity
public class LectureGroup implements Serializable {

    private static final long serialVersionUID = -1120705855748906219L;
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Lecture lecture;

    @ManyToOne(fetch=FetchType.EAGER)
    private StudyGroup studyGroup;
    private String fraction = "";

    public LectureGroup(Lecture lecture, StudyGroup studyGroup, String fraction) {
        this.lecture = lecture;
        this.studyGroup = studyGroup;
        this.fraction = fraction;
    }
    
    LectureGroup(){}

    public Integer getId() {
        return id;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public String getFraction() {
        return fraction;
    }

    @Override
    public String toString() {
        return studyGroup.getProgram() + Integer.toString(studyGroup.getSemester()) + fraction +
        " <--> " + lecture.getName();
    }

}
