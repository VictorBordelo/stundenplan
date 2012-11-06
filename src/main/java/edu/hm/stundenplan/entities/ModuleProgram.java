package edu.hm.stundenplan.entities;

import java.io.*;

import javax.persistence.*;

@Entity
public class ModuleProgram  implements Serializable {

    @EmbeddedId
    private Id id;

    @ManyToOne
    @JoinColumn(name = "moduleid", insertable = false, updatable = false)
    private Module module;

    @ManyToOne
    @JoinColumn(name = "programid", insertable = false, updatable = false)
    private DegreeProgram program;
    private int semester;
    
    public ModuleProgram(){}

    public ModuleProgram(Module module, DegreeProgram program, int semester) {
        id = new Id(module.getId(), program.getId());
        this.module = module;
        this.program = program;
        this.semester = semester;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Module getModule() {
        return module;
    }

    public DegreeProgram getProgram() {
        return program;
    }
    
    @Override
    public String toString() {
        return module + " " + program + " " + semester;
    }
    
    @Embeddable
    static class Id implements Serializable {
        
        @Column(name = "moduleid")
        private int moduleId;
        @Column(name = "programid")
        private int programId;
        
        Id(){}
        
        Id(int moduleId, int programId) {
            this.moduleId = moduleId;
            this.programId = programId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + moduleId;
            result = prime * result + programId;
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
            Id other = (Id) obj;
            if (moduleId != other.moduleId)
                return false;
            if (programId != other.programId)
                return false;
            return true;
        }
    }
    
}

