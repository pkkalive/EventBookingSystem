/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.active_record;

import databaseConnectivity.ConnectionManager;
import databaseConnectivity.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Purushotham
 */
public class VenueHandler
{
    private static final String FIND_ALL_VENUES = "SELECT Venue.*, "
            +      "EventManagerVenue.VenueName AS pVenueName, EventManagerVenue.VenueCity AS pVenueCity, "
            +      "EventManager.* "
            + "FROM Venue LEFT JOIN EventManagerVenue ON Venue.VenueId = EventManagerVenue.VenueId "
            + "LEFT JOIN EventManager ON EventManagerVenue.EmpId = EventManager.EmpId "
            + "ORDER BY Venue.VenueId, EventManager.EmpId";
    private static final String FIND_VENUE_BY_ID = 
            "SELECT Venue.*, "
            +      "EventManagerVenue.VenueName AS pVenueName, EventManagerVenue.VenueCity AS pVenueCity, "
            +      "EventManager.* "
            + "FROM Venue JOIN EventManagerVenue ON Venue.VenueId = EventManagerVenue.VenueId "
            + "JOIN EventManager ON EventManagerVenue.EmpId = EventManager.EmpId "
            + "WHERE Venue.VenueId=? "
            + "ORDER BY Venue.VenueId, EventManager.EmpId";
    private static final String FIND_VENUES_BY_NAME = "SELECT Venue.*, "
            +      "EventManagerVenue.VenueName AS pVenueName, EventManagerVenue.VenueCity AS pVenueCity, "
            +      "EventManager.* "
            + "FROM Venue JOIN EventManagerVenue ON Venue.VenueId = EventManagerVenue.VenueId "
            + "JOIN EventManager ON EventManagerVenue.EmpId = EventManager.EmpId "
            + "WHERE VenueName=? "
            + "ORDER BY Venue.VenueId, EventManager.EmpId";
    private static final String FIND_VENUES_FOR_EVENTMANAGER = "SELECT Venue.*, "
            +      "EventManagerVenue.VenueName AS pVenueName, EventManagerVenue.VenueCity AS pVenueCity, "
            +      "EventManager.* "
            + "FROM Venue JOIN EventManagerVenue ON Venue.VenueId = EventManagerVenue.VenueId "
            + "JOIN EventManager ON EventManagerVenue.EmpId = EventManager.EmpId "
            + "WHERE EventManager.EmpId=? "
            + "ORDER BY Venue.VenueId, EventManager.EmpId";
    
    public boolean createVenue(Venue s)
    {
        boolean venueCreated = false;
        ArrayList<Venue> venues = findVenuesByName(s.getVenueName());
        boolean found = false;
        
        for (Venue venue: venues)
        {
            if (venue.getVenueName().equals(s.getVenueName()) &&
                    venue.getVenueName().equals(s.getVenueName()))
            {
                found = true;
            }
        }
        
        if (!found)
        {
            try
            {
                s.insert();
                venueCreated = true;
            }
            catch (Exception e)
            {
            }
        }
        return venueCreated;
    }

    public boolean publishVenue(Venue venue)
    {
        boolean venuePublished = false;
        
        if (venue != null)
        {
            venue.setPublished(true);
            try
            {
                venue.update();
                venuePublished = true;
            }
            catch (Exception e)
            {
            }
        }
        return venuePublished;
    }

    public boolean deleteVenue(Venue venue)
    {
        boolean venueDeleted = false;
        
        if (venue != null)
        {
            try
            {
                venue.delete();
                venueDeleted = true;
            }
            catch (Exception e)
            {
            }
        }
        return venueDeleted;
    }

    private EventManagerVenue prepareEventManagerVenue(ResultSet rs) throws SQLException
    {
        EventManager p = new EventManager(rs.getInt("EmpId"), rs.getString("Name"), rs.getString("Username"), rs.getString("Password"));
        return new EventManagerVenue(rs.getInt("VenueId"), p, rs.getString("pVenueName"), rs.getString("pVenueCity"));
    }

    private Venue prepareVenue(ResultSet rs)
    {
        Venue venue = null;

        try
        {
            if (rs.next())
            {
                venue = new Venue(rs.getInt("VenueId"),
                                  rs.getBoolean("IsPublished"),
                                  rs.getString("VenueName"),
                                  rs.getString("VenueCity"));
                
                venue.addEventManagerVenue(prepareEventManagerVenue(rs));
                while (rs.next())
                {
                    venue.addEventManagerVenue(prepareEventManagerVenue(rs));
                }
            }
        }
        catch (SQLException sqle)
        {
        }

        return venue;
    }

    private ArrayList<Venue> prepareVenues(ResultSet rs)
    {
        ArrayList<Venue> list = new ArrayList();

        try
        {
            if (rs.next())
            {
                int venueId = rs.getInt("VenueId");
                Venue venue = new Venue(venueId,
                                        rs.getBoolean("IsPublished"),
                                        rs.getString("VenueName"),
                                        rs.getString("VenueCity"));
             
                list.add(venue);
                EventManagerVenue  ps = prepareEventManagerVenue(rs);
                if (ps != null && ps.getEmployeeId()> 0)
                {
                    venue.addEventManagerVenue(ps);
                }
                while (rs.next())
                {
                    int nextVenueId = rs.getInt("VenueId");
                    if (nextVenueId != venueId)
                    {
                        venueId = nextVenueId;
                        venue = new Venue(venueId,
                                          rs.getBoolean("IsPublished"),
                                          rs.getString("VenueName"),
                                          rs.getString("VenueCity"));

                        list.add(venue);
                    }
                    ps = prepareEventManagerVenue(rs);
                    if (ps != null && ps.getEmployeeId()> 0)
                    {
                        venue.addEventManagerVenue(ps);
                }
                }
            }
        }
        catch (SQLException sqle)
        {
        }

        return list;
    }

    public ArrayList<Venue> findAllVenues()
    {
        ArrayList<Venue> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_ALL_VENUES);
        if (con.executePreparedStatement())
        {
            list = prepareVenues(con.getResultSet());
        }
        con.close();

        return list;
    }
    
    public Venue findVenueById(int venueId)
    {
        Venue sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_VENUE_BY_ID);
        con.setStatementParameter(1, venueId);
        if (con.executePreparedStatement())
        {
            sp = prepareVenue(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<Venue> findVenuesByName(String name)
    {
        ArrayList<Venue> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_VENUES_BY_NAME);
        con.setStatementParameter(1, name);
        if (con.executePreparedStatement())
        {
            list = prepareVenues(con.getResultSet());
        }
        con.close();

        return list;
    }

    public ArrayList<Venue> findVenuesForEventManager(int empId)
    {
        ArrayList<Venue> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_VENUES_FOR_EVENTMANAGER);
        con.setStatementParameter(1, empId);
        if (con.executePreparedStatement())
        {
            list = prepareVenues(con.getResultSet());
        }
        con.close();

        return list;
    }
}
