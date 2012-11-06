package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;

@Entity
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    private int sws;
    private int ects;
    private String name;
   
    public Module(String name, Integer sws, Integer ects) {
        this.sws = sws;
        this.ects = ects;
        this.name = name;
    }

    public Module(){}

    public int getSws() {
        return sws;
    }

    public void setSws(int sws) {
        this.sws = sws;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return name + " (" + sws + " SWS, " + ects + " ECTS)";
    }
    
    
}
