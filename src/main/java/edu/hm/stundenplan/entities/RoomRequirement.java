package edu.hm.stundenplan.entities;

import javax.persistence.*;

@Entity
public class RoomRequirement {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @ManyToOne(fetch=FetchType.EAGER)
    private RoomType requirementType;
    private int requiredNumber;
    
    public RoomRequirement() {}
    
    public RoomRequirement(RoomType requirementType, int requiredNumber) {
        this.requirementType = requirementType;
        this.requiredNumber = requiredNumber;
    }

    public RoomType getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(RoomType requirementType) {
        this.requirementType = requirementType;
    }

    public int getRequiredNumber() {
        return requiredNumber;
    }

    public void setRequiredNumber(int requiredNumber) {
        this.requiredNumber = requiredNumber;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + requiredNumber;
        result = prime * result
                + ((requirementType == null) ? 0 : requirementType.hashCode());
        return result;
    }

    /** We consider requirements as equal when the type of requirement is identical
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoomRequirement other = (RoomRequirement) obj;
        if (requirementType == null) {
            if (other.requirementType != null)
                return false;
        } else if (!requirementType.equals(other.requirementType))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return requirementType.getName() + " " + requirementType.getDescription() + " (" + requiredNumber + ")";
    }

}
