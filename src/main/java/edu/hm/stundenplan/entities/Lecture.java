package edu.hm.stundenplan.entities;

import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@XmlRootElement
public final class Lecture {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Module module;
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Room> rooms = new HashSet<Room>();
    @OneToMany(fetch=FetchType.EAGER)
    private Set<RoomRequirement> roomRequirements = new HashSet<RoomRequirement>();
    @ManyToOne(fetch=FetchType.EAGER)
    private Teacher teacher;
    @OneToMany(mappedBy="lecture", fetch=FetchType.EAGER)
    private Set<LectureGroup> lectureGroupMappings = new HashSet<LectureGroup>();
    private WeekDay day;
    private TimeSlot slot;
    private int duration;
    private int numberOfParticipants;

    protected Lecture() {}
    
    public Lecture(Module module, Teacher teacher) {
        this.module = module;
        this.teacher = teacher;
    }
    
    /**
     * returns the teacher of the lecture.
     * 
     * @author Benedikt Zönnchen
	 * @return the teacher of the lecture
	 */
	public Teacher getTeacher()
	{
		return teacher;
	}

	public Integer getId() {
        return id;
    }
    
    public WeekDay getDay() {
        return day;
    }
    
    public String getName() {
        return module.getName();
    }
    
    public void setDay(WeekDay day) {
        this.day = day;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setStart(TimeSlot start) {
        this.slot = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public Module getModule() {
        return module;
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public boolean removeRoom(Room room) {
        return rooms.remove(room);
    }
    
    public Set<Room> getRooms() {
        return rooms;
    }
    
    public void addRoomRequirement(RoomRequirement r) {
        roomRequirements.add(r);
    }
    
    public boolean removeRoomRequirement(RoomRequirement r) {
        return roomRequirements.remove(r);
    }
    
    public Set<RoomRequirement> getRoomRequirements() {
        return Collections.unmodifiableSet(roomRequirements);
    }
    
    public Set<LectureGroup> getLectureGroupMappings() {
        return Collections.unmodifiableSet(lectureGroupMappings);
    }
    
    @Override
    public String toString() {
        return "(" + this.id + ") " + module.getName() + ": " + (teacher != null ? teacher.getName() + ", " + teacher.getVorname() : "") + " " + day + " " + slot + " " + duration;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + duration;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((slot == null) ? 0 : slot.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lecture other = (Lecture) obj;
        if (day != other.day)
            return false;
        if (duration != other.duration)
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (slot == null) {
            if (other.slot != null)
                return false;
        } else if (!slot.equals(other.slot))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }
    
}
