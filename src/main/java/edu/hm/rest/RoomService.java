package edu.hm.rest;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.*;
import org.hibernate.criterion.*;

import edu.hm.rest.ProfService.*;
import edu.hm.stundenplan.*;
import edu.hm.stundenplan.entities.*;

@Path("/json/rooms")
public class RoomService {

    @GET
    @Path("/roomList/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Room[] getRoomListAsJSON() {

        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<Room> allRooms = session.createQuery(
                "from Room order by name").list();
        tx.commit();
        return allRooms.toArray(new Room[0]);
    }
    
    /**
     * Produces all roomtypes that do not solely contain the single room they represent as type.
     * @return list of roomtypes
     */
    @GET
    @Path("/roomTypeList/get")
    @Produces(MediaType.APPLICATION_JSON)
    public RoomType[] getRoomTypesAsJSON() {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("unchecked")
        List<RoomType> allTypes = session.createQuery(
                "from RoomType order by name").list();
        for (Iterator<RoomType> iterator = allTypes.iterator(); iterator.hasNext();) {
            RoomType roomType = (RoomType) iterator.next();
            List<Room> rooms = roomType.getRooms();
            if(rooms.size() == 1 && rooms.get(0).getName().equalsIgnoreCase(roomType.getName())) {
                iterator.remove();
            }
        }
        tx.commit();
        return allTypes.toArray(new RoomType[0]);
    }
    
    @PUT
    @Path("/availability/put/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setAvailability(SimpleAvailabilityDto roomDto) {
        Session session = ProductionSessionFactory.getDefault().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Room.class).add(Restrictions.eq("id", roomDto.getId()));
        Room room = (Room) criteria.uniqueResult();
        room.setAvailability(roomDto.getAvailability());
        session.save(room);
        tx.commit();
    }
    
}
