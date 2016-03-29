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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
 *
 * @author Purushotham
 */
public class Event implements ActiveRecord
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat stf = new SimpleDateFormat("hh:mm");

    private static final String GET_EVENT_ID = "SELECT EventId FROM Event WHERE EventName=? AND EventDate=? AND StartTime=? AND EndTime=?";
    private static final String INSERT_EVENT = "INSERT INTO Event(IsPublished, EventName, EventDate, StartTime, EndTime, Cost) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_EVENT
            = "UPDATE Event "
            + "SET IsPublished=?, EventName=?, EventDate=?, StartTime=?, EndTime=?, Cost=? "
            + "WHERE EventId=?";
    private static final String DELETE_EVENT = "DELETE FROM Event WHERE EventId=?";

    private int eventId;
    private boolean published;
    private String eventName;
    private Date eventDate;
    private Time startTime;
    private Time endTime;
    private int cost;

    private HashMap<Integer, Event> eventDetails;

    public Event(String eventName, Date eventDate, Time startTime, Time endTime, int cost)
    {
        this(-1, false, eventName, eventDate, startTime, endTime, cost);
    }

    public Event(int eventId, boolean published, String eventName, Date eventDate, Time startTime, Time endTime, int cost)
    {
        this.eventId = eventId;
        this.published = published;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;

//        personEvents = new HashMap<>();
    }

    public int getEventId()
    {
        return eventId;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished(boolean b)
    {
        this.published = b;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String en)
    {
        this.eventName = en;
    } 
    
    public Date getEventDate()
    {
        return eventDate;
    }

    public void setEventDate(Date d)
    {
        this.eventDate = d;
    }

    public Time getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Time t)
    {
        this.startTime = t;
    }

    public Time getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Time t)
    {
        this.endTime = t;
    }
    
     public int getCost()
    {
        return cost;
    }

    public void setCost(int c)
    {
        this.cost = c;
    }
    
    public Event getEventDetails(int eventId)
    {
        return eventDetails.get(eventId);
    }

    @Override
    public String toString()
    {
        return eventId + ":"
                + (published ? "published" : "unpublished") + ":"
                + eventName + ":"
                + sdf.format(eventDate) + ":"
                + stf.format(startTime) + "-"
                + stf.format(endTime) + ":"
                + cost;
    }

//    public boolean addPersonEvent(PersonEvent ps)
//    {
//        boolean personEventAdded = false;
//
//        if (!personEvents.containsKey(ps.getSalesPerson().getEmployeeId()))
//        {
//            personEvents.put(ps.getSalesPerson().getEmployeeId(), ps);
//            personEventAdded = true;
//        }
//
//        return personEventAdded;
//    }

//    public boolean addSalesPerson(EventManager p)
//    {
//        boolean personAdded = false;
//
//        if (!personEvents.containsKey(p.getEmployeeId()))
//        {
//            PersonEvent ps = new PersonEvent(eventId, p);
//            try
//            {
//                ps.insert();
//                personEvents.put(p.getEmployeeId(), ps);
//                personAdded = true;
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        return personAdded;
//    }

//    public boolean deleteSalesPerson(EventManager p)
//    {
//        boolean personDeleted = false;
//
//        if (personEvents.containsKey(p.getEmployeeId()))
//        {
//            PersonEvent ps = personEvents.get(p.getEmployeeId());
//            try
//            {
//                ps.delete();
//                personEvents.remove(p.getEmployeeId());
//                personDeleted = true;
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        return personDeleted;
//    }

//    public HashMap<Integer, PersonEvent> getPersonEvents()
//    {
//        return personEvents;
//    }
//    
//    public PersonEvent[] getPersonEventsAsArray()
//    {
//        return personEvents.values().toArray(new PersonEvent[0]);
//    }
//    
//    public PersonEvent getPersonEvent(int empId)
//    {
//        return personEvents.get(empId);
//    }

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_EVENT);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, eventName);
        con.setStatementParameter(3, eventDate);
        con.setStatementParameter(4, startTime);
        con.setStatementParameter(5, endTime);
        con.setStatementParameter(6, cost);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Insertion failed for: " + this);
        }
        con.prepareStatement(GET_EVENT_ID);
        con.setStatementParameter(1, eventName);
        con.setStatementParameter(2, eventDate);
        con.setStatementParameter(3, startTime);
        con.setStatementParameter(4, endTime);
        con.setStatementParameter(5, cost);
        if (con.executePreparedStatement())
        {
            try
            {
                ResultSet rs = con.getResultSet();
                if (rs.next())
                {
                    eventId = rs.getInt("EventId");
                }
            }
            catch (SQLException sqle)
            {

            }
        }
//        for (Integer key : personEvents.keySet())
//        {
//            personEvents.get(key).insert();
//        }
        con.close();
    }

    @Override
    public void update() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_EVENT);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, eventName);
        con.setStatementParameter(3, eventDate);
        con.setStatementParameter(4, startTime);
        con.setStatementParameter(5, endTime);
        con.setStatementParameter(6, cost);
        con.setStatementParameter(7, eventId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Update failed for: " + this);
        }
//        for (Integer key : personEvents.keySet())
//        {
//            personEvents.get(key).update();
//        }
        con.close();
    }

    @Override
    public void delete() throws Exception
    {
        //relies on the database cascading the deletion to the Event records
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(DELETE_EVENT);
        con.setStatementParameter(1, eventId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();

//        personEvents = null;
    }
}