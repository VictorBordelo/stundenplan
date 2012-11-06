package edu.hm.stundenplan.entities;

import java.util.*;

import javax.persistence.*;

@Entity
public class RoomType {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    @ManyToMany(fetch=FetchType.EAGER)
    private List<Room> rooms = new ArrayList<Room>();
    
    RoomType(){}

    public RoomType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean removeRoom(Room room) {
        return rooms.remove(room);
    }
    
    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if(rooms.size() > 0) {
            sb.append(" (");
            for (Room room : rooms) {
                sb.append(room.getName());
                sb.append(" ");
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((rooms == null) ? 0 : rooms.hashCode());
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
        RoomType other = (RoomType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (rooms == null) {
            if (other.rooms != null)
                return false;
        } else if (!rooms.equals(other.rooms))
            return false;
        return true;
    }

}
