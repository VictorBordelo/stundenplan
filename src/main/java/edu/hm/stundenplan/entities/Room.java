package edu.hm.stundenplan.entities;

import javax.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private int size;
    /**
     * The availability information is intended to be a 35-character string
     * One character per timetable slot. '0' means available. Anything else might receive different semantics in the future.
     */
    private String availability;
    
    public Room(String name) {
        this.name = name;
    }
    
    public Room(String name, int size) {
        this.name = name;
        this.size = size;
    }
    
    public Room() {}
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
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
        return name;
    }

}
