package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;

@Entity
public class DegreeProgram implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    
    private String title;
    private String description;
    
    public DegreeProgram(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected DegreeProgram(){}

    public String getTitle() {
        return title;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description + " (" + title + ")";
    }

}
