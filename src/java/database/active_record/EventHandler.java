/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.active_record;

import databaseConnectivity.ConnectionManager;
import databaseConnectivity.DatabaseConnection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Purushotham
 */
public class EventHandler 
{
    private static final String FIND_ALL_EVENTS = "SELECT * FROM Event";
    private static final String FIND_EVENT_BY_ID = "SELECT * FROM Event WHERE EmpId=?";
    private static final String FIND_EVENTS_BY_DATE = "SELECT * FROM Event WHERE EventDate=?";
    private static final String FIND_EVENTS_FOR_SALESPERSON = "SELECT Event.*, "
            +      "Event.StartTime AS pStartTime, Event.EndTime AS pEndTime, "
            +      "EventManager.* "
            + "FROM Event JOIN PersonEvent ON Event.EventId = PersonEvent.EventId "
            + "JOIN EventManager ON PersonEvent.EmpId = EventManager.EmpId "
            + "WHERE EventManager.EmpId=? "
            + "ORDER BY Event.EventId, EventManager.EmpId";
    
    public boolean createEvent(Event s)
    {
        boolean eventCreated = false;
        ArrayList<Event> events = findEventsByDate(s.getEventDate());
        boolean found = false;
        
        for (Event event: events)
        {
            if (event.getStartTime().equals(s.getStartTime()) &&
                    event.getEndTime().equals(s.getEndTime()))
            {
                found = true;
            }
        }
        
        if (!found)
        {
            try
            {
                s.insert();
                eventCreated = true;
            }
            catch (Exception e)
            {
            }
        }
        return eventCreated;
    }

    public boolean publishEvent(Event event)
    {
        boolean eventPublished = false;
        
        if (event != null)
        {
            event.setPublished(true);
            try
            {
                event.update();
                eventPublished = true;
            }
            catch (Exception e)
            {
            }
        }
        return eventPublished;
    }

    public boolean deleteEvent(Event event)
    {
        boolean eventDeleted = false;
        
        if (event != null)
        {
            try
            {
                event.delete();
                eventDeleted = true;
            }
            catch (Exception e)
            {
            }
        }
        return eventDeleted;
    }

//    private Event prepareEvent(ResultSet rs) throws SQLException
//    {
//        EventManager p = new EventManager(rs.getInt("EmpId"), rs.getString("Name"), rs.getString("Username"), rs.getString("Password"));
//        return new Event(rs.getInt("EventId"),rs.getBoolean("IsPublished"), rs.getString("VeueName"), rs.getDate("EventDate"),
//                                  rs.getTime("StartTime"),
//                                  rs.getTime("EndTime"),
//                                  rs.getInt("Cost"));
//    }

    private Event prepareEvent(ResultSet rs)
    {
        Event event = null;

        try
        {
            if (rs.next())
            {
                event = new Event(rs.getInt("EventId"),
                                  rs.getBoolean("IsPublished"),
                                  rs.getString("VeueName"),
                                  rs.getDate("EventDate"),
                                  rs.getTime("StartTime"),
                                  rs.getTime("EndTime"),
                                  rs.getInt("Cost"));
            }
        }
        catch (SQLException sqle)
        {
        }

        return event;
    }

    private ArrayList<Event> prepareEvents(ResultSet rs)
    {
        ArrayList<Event> list = new ArrayList();

        try
        {
            if (rs.next())
            {
                int eventId = rs.getInt("EventId");
                Event event = new Event(eventId,
                                        rs.getBoolean("IsPublished"),
                                        rs.getString("EventName"),
                                        rs.getDate("EventDate"),
                                        rs.getTime("StartTime"),
                                        rs.getTime("EndTime"),
                                        rs.getInt("Cost"));
             
                list.add(event);
                while (rs.next())
                {
                    int nextEventId = rs.getInt("EventId");
                    if (nextEventId != eventId)
                    {
                        eventId = nextEventId;
                        event = new Event(eventId,
                                        rs.getBoolean("IsPublished"),
                                        rs.getString("EventName"),
                                        rs.getDate("EventDate"),
                                        rs.getTime("StartTime"),
                                        rs.getTime("EndTime"),
                                        rs.getInt("Cost"));

                        list.add(event);
                    }
                   
                }
            }
        }
        catch (SQLException sqle)
        {
        }

        return list;
    }

    public ArrayList<Event> findAllEvents()
    {
        ArrayList<Event> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_ALL_EVENTS);
        if (con.executePreparedStatement())
        {
            list = prepareEvents(con.getResultSet());
        }
        con.close();

        return list;
    }
    
    public Event findEventById(int eventId)
    {
        Event sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EVENT_BY_ID);
        con.setStatementParameter(1, eventId);
        if (con.executePreparedStatement())
        {
            sp = prepareEvent(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<Event> findEventsByDate(Date date)
    {
        ArrayList<Event> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EVENTS_BY_DATE);
        con.setStatementParameter(1, date);
        if (con.executePreparedStatement())
        {
            list = prepareEvents(con.getResultSet());
        }
        con.close();

        return list;
    }

    public ArrayList<Event> findEventsForSalesPerson(int empId)
    {
        ArrayList<Event> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EVENTS_FOR_SALESPERSON);
        con.setStatementParameter(1, empId);
        if (con.executePreparedStatement())
        {
            list = prepareEvents(con.getResultSet());
        }
        con.close();

        return list;
    }
}
