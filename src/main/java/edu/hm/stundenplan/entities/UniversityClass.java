package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;

@Entity
public class UniversityClass implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private int semester;
    private char subGroup;
    @OneToOne
    @Embedded
    private DegreeProgram degreeProgram;
    
    public UniversityClass(int semester, char group, DegreeProgram degreeProgram) {
        this.semester = semester;
        this.subGroup = group;
        this.degreeProgram = degreeProgram;
    }
    
    protected UniversityClass(){}
    
    public int getSemester() {
        return semester;
    }
    
    public char getSubGroup() {
        return subGroup;
    }

    public DegreeProgram getDegreeProgram() {
        return degreeProgram;
    }
}
