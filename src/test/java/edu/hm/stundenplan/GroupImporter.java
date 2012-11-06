package edu.hm.stundenplan;

import edu.hm.stundenplan.dao.*;
import edu.hm.stundenplan.entities.*;

public class GroupImporter {
    
    DegreeProgram gob = new DegreeProgram("GO", "Bachelor Geotelematik und Navigation");
    DegreeProgram ifb = new DegreeProgram("IF", "Bachelor Informatik");
    DegreeProgram icb = new DegreeProgram("IC", "Bachelor Scientific Computing");
    DegreeProgram ibb = new DegreeProgram("IB", "Bachelor Wirtschaftsinformatik");
    DegreeProgram igm = new DegreeProgram("IG", "Master Informatik");
    DegreeProgram ism = new DegreeProgram("IS", "Master Stochastic Engineering in Business and Finance");
    DegreeProgram inm = new DegreeProgram("IN", "Master Wirtschaftsinformatik");
    
    DegreeProgram[] programs = new DegreeProgram[] {
            gob, ifb, icb, ibb, igm, ism, inm, 
    };
    
    StudyGroup[] groups = new StudyGroup[]  {
            new StudyGroup(gob, 1, ""),
            new StudyGroup(gob, 3, ""),
            new StudyGroup(gob, 5, ""),
            new StudyGroup(gob, 7, ""),
            new StudyGroup(ifb, 1, "A"),
            new StudyGroup(ifb, 1, "B"),
            new StudyGroup(ifb, 1, "C"),
            new StudyGroup(ifb, 3, "A"),
            new StudyGroup(ifb, 3, "B"),
            new StudyGroup(ifb, 4, ""),
            new StudyGroup(ifb, 5, ""),
            new StudyGroup(ifb, 6, ""),
            new StudyGroup(ifb, 7, ""),
            new StudyGroup(ibb, 1, "A"),
            new StudyGroup(ibb, 1, "B"),
            new StudyGroup(ibb, 1, "C"),
            new StudyGroup(ibb, 3, "A"),
            new StudyGroup(ibb, 3, "B"),
            new StudyGroup(ibb, 3, "C"),
            new StudyGroup(ibb, 5, "A"),
            new StudyGroup(ibb, 5, "B"),
            new StudyGroup(ibb, 5, "C"),
            new StudyGroup(ibb, 7, "A"),
            new StudyGroup(ibb, 7, "B"),
            new StudyGroup(ibb, 7, "C"),

            new StudyGroup(icb, 1, ""),
            new StudyGroup(icb, 3, ""),
            new StudyGroup(icb, 5, ""),
            new StudyGroup(icb, 6, ""),
            new StudyGroup(inm, 1, ""),
            new StudyGroup(inm, 2, ""),
            new StudyGroup(inm, 3, ""),
            new StudyGroup(igm, 1, ""),
            new StudyGroup(igm, 2, ""),
            new StudyGroup(ism, 3, ""),
            new StudyGroup(ism, 1, ""),
            new StudyGroup(ism, 2, ""),
    };

    /** brute force generation of all groups
     */
    public void importGroups() {
        GenericDao dao;
        for (int i = 0; i < programs.length; i++) {
            dao = new GenericDao<DegreeProgram>() {};
            dao.insert(programs[i]);
        }
        for (int i = 0; i < groups.length; i++) {
            groups[i].setAvailability(PersonImporter.DEFAULT_AVAILABILITY);
            dao = new GenericDao<StudyGroup>() {};
            dao.insert(groups[i]);
        }
    }

}
