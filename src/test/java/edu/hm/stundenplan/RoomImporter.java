package edu.hm.stundenplan;

import java.io.*;
import java.util.*;

import edu.hm.stundenplan.dao.*;
import edu.hm.stundenplan.entities.*;

public class RoomImporter {

    private String file;

    public RoomImporter(String file) {
        this.file = file;
    }

    public void importRooms() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
        String line = reader.readLine();
        RoomType hs = new RoomType("Hörsaal", "Hörsaal");
        RoomType lab = new RoomType("Labor", "Labor");
        RoomType sem = new RoomType("Seminarraum", "Seminarraum");
        while(line != null) {
            StringTokenizer stt = new StringTokenizer(line, "\t");
            String name = stt.nextToken();
            int size = Integer.parseInt(stt.nextToken());
            String rType = stt.nextToken();
            RoomType roomType = new RoomType(name, "");
            Room room = new Room(name, size);
            room.setAvailability(PersonImporter.DEFAULT_AVAILABILITY);
            roomType.addRoom(room);
            GenericDao<RoomType> roomTypeDao = new GenericDao<RoomType>("name"){};
            GenericDao<Room> roomDao = new GenericDao<Room>("name"){};
            roomDao.insert(room);
            roomTypeDao.insert(roomType);
            if("Labor".equals(rType)) {
                lab.addRoom(room);
            }
            else if("Seminarraum".equals(roomType)) {
                sem.addRoom(room);
            }
            else {
                hs.addRoom(room);
            }
            line = reader.readLine();
        }
        GenericDao<RoomType> lhTypeDao = new GenericDao<RoomType>("name"){};
        lhTypeDao.insert(hs);
        lhTypeDao.insert(lab);
        lhTypeDao.insert(sem);
    }

}
