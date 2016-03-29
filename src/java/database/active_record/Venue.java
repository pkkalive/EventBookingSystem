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
import java.util.HashMap;

/**
 *
 * @author Purushotham
 */
public class Venue implements ActiveRecord
{
    private static final String GET_VENUE_ID = "SELECT VenueId FROM Venue WHERE VenueName=? AND VenueCity=?";
    private static final String INSERT_VENUE = "INSERT INTO Venue( IsPublished, VenueName, VenueCity) VALUES (?, ?, ?)";
    private static final String UPDATE_VENUE
            = "UPDATE Venue "
            + "SET  IsPublished=?, VenueName=?, VenueCity=?"
            + "WHERE VenueId=?";
    private static final String DELETE_VENUE = "DELETE FROM Venue WHERE VenueId=?";

    private int venueId;
    private boolean published;
    private String venueName;
    private String venueCity;
    

    private HashMap<Integer, EventManagerVenue> managerVenues;

    public Venue(String venueName, String venueCity)
    {
        this(-1,false, venueName, venueCity );
    }

    public Venue(int venueId, boolean published, String venueName, String venueCity)
    {
        this.venueId = venueId;
        this.published = published;
        this.venueName = venueName;
        this.venueCity = venueCity;
        
        
        managerVenues = new HashMap<>();
    }

    public int getVenueId()
    {
        return venueId;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished(boolean b)
    {
        this.published = b;
    }

    public String getVenueName()
    {
        return venueName;
    }

    public void setVenueName(String vn)
    {
        this.venueName = vn;
    }

    public String getVenueCity()
    {
        return venueCity;
    }

    public void setVenueCity(String vcity)
    {
        this.venueCity = vcity;
    }

    @Override
    public String toString()
    {
        return venueId + ":"
                + (published ? "published" : "unpublished") + ":"
                + venueName + ":"
                + venueCity;
    }

    protected boolean addEventManagerVenue(EventManagerVenue ps)
    {
        boolean EventManagerVenueAdded = false;

        if (!managerVenues.containsKey(ps.getEventManager().getEmployeeId()))
        {
            managerVenues.put(ps.getEventManager().getEmployeeId(), ps);
            EventManagerVenueAdded = true;
        }

        return EventManagerVenueAdded;
    }

    public boolean addEventManager(EventManager p)
    {
        boolean personAdded = false;

        if (!managerVenues.containsKey(p.getEmployeeId()))
        {
            EventManagerVenue ps = new EventManagerVenue(venueId, p);
            try
            {
                ps.insert();
                managerVenues.put(p.getEmployeeId(), ps);
                personAdded = true;
            }
            catch (Exception e)
            {
            }
        }

        return personAdded;
    }

    public boolean deleteEventManager(EventManager p)
    {
        boolean personAdded = false;

        if (managerVenues.containsKey(p.getEmployeeId()))
        {
            EventManagerVenue ps = managerVenues.get(p.getEmployeeId());
            try
            {
                ps.delete();
                managerVenues.remove(p.getEmployeeId());
                personAdded = true;
            }
            catch (Exception e)
            {
            }
        }

        return personAdded;
    }

    public HashMap<Integer, EventManagerVenue> getEventManagerVenues()
    {
        return managerVenues;
    }
    
    public EventManagerVenue[] getEventManagerVenuesAsArray()
    {
        return managerVenues.values().toArray(new EventManagerVenue[0]);
    }
    
    public EventManagerVenue getEventManagerVenue(int empId)
    {
        return managerVenues.get(empId);
    }

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_VENUE);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, venueName);
        con.setStatementParameter(3, venueCity);
        
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Insertion failed for: " + this);
        }
        con.prepareStatement(GET_VENUE_ID);
        con.setStatementParameter(1, venueName);
        con.setStatementParameter(2, venueCity);
        
        if (con.executePreparedStatement())
        {
            try
            {
                ResultSet rs = con.getResultSet();
                if (rs.next())
                {
                    venueId = rs.getInt("VenueId");
                }
            }
            catch (SQLException sqle)
            {

            }
        }
        for (Integer key : managerVenues.keySet())
        {
            managerVenues.get(key).insert();
        }
        con.close();
    }

    @Override
    public void update() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_VENUE);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, venueName);
        con.setStatementParameter(3, venueCity);
        con.setStatementParameter(4, venueId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Update failed for: " + this);
        }
        for (Integer key : managerVenues.keySet())
        {
            managerVenues.get(key).update();
        }
        con.close();
    }

    @Override
    public void delete() throws Exception
    {
        //relies on the database cascading the deletion to the EventManagerVenue records
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(DELETE_VENUE);
        con.setStatementParameter(1, venueId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();

        managerVenues = null;
    }
    
    public void venueUpdate() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_VENUE);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, venueName);
        con.setStatementParameter(3, venueCity);
        con.setStatementParameter(4, venueId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Update failed for: " + this);
        }
//        for (Integer key : managerVenues.keySet())
//        {
//            managerVenues.get(key).update();
//        }
        con.close();
    }
}
