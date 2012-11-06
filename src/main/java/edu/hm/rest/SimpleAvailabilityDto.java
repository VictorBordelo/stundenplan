package edu.hm.rest;

/**
 * simple DTO class to transport availability information and an entity's id.
 * @author axel
 *
 */
public class SimpleAvailabilityDto {
    
    private int id;
    private String availability;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public String toString() {
        return id + " " + availability;
    }

}
