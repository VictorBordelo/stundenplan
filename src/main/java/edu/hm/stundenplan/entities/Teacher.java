package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@XmlRootElement
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private static final long serialVersionUID = -5500885674631981243L;
    private String name;
    private String vorname;
    private String sex;
    private TeacherStatus status;
    /**
     * The availability information is intended to be a 35-character string
     * One character per timetable slot. '0' means available. Anything else might receive different semantics in the future.
     */
    private String availability;
    
    public Integer getId() {
        return id;
    }

    protected Teacher() {}
    
    public Teacher(String name, String vorname, String sex, TeacherStatus status) {
        this.name = name;
        this.vorname = vorname;
        this.sex = sex;
        this.status = status;
    }

    public Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public String getVorname() {
        return vorname;
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
    
    public String toString() {
        return name + " " + vorname;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
        Teacher other = (Teacher) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sex == null) {
            if (other.sex != null)
                return false;
        } else if (!sex.equals(other.sex))
            return false;
        if (status != other.status)
            return false;
        if (vorname == null) {
            if (other.vorname != null)
                return false;
        } else if (!vorname.equals(other.vorname))
            return false;
        return true;
    }

}
