/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.active_record;

import databaseConnectivity.ConnectionManager;
import databaseConnectivity.DatabaseConnection;


/**
 *
 * @author Purushotham
 */
public class EventManagerVenue implements ActiveRecord
{
    private static final String INSERT_EVENTMANAGERVENUE = "INSERT INTO EventManagerVenue(VenueId, EmpId, VenueName, VenueCity) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_EVENTMANAGERVENUE = "UPDATE EventManagerVenue " +
                                                            "SET VenueName=?, VenueCity=?" + 
                                                            "WHERE venueId=? AND empId=?";
    private static final String DELETE_EVENTMANAGER = "DELETE FROM EventManagerVenue WHERE venueId=? AND empId=?";

    private int venueId;
    private EventManager person;
    private String venueName;
    private String venueCity;
    
    

    public EventManagerVenue(int venueId, EventManager p)
    {
        this(venueId, p, null, null); 
   }

    public EventManagerVenue(int venueId, EventManager p, String venueName, String venueCity)
    {
        this.venueId = venueId;
        this.person = p;
        this.venueName = venueName;
        this.venueCity = venueCity;
        
        
   }

    public int getVenueId()
    {
        return venueId;
    }

    public EventManager getEventManager()
    {
        return person;
    }

    public int getEmployeeId()
    {
        return person.getEmployeeId();
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
        return getVenueId() + ":" + getEmployeeId() + ":" + getVenueName()+ ":" + getVenueCity();
    }

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_EVENTMANAGERVENUE);
        con.setStatementParameter(1, venueId);
        con.setStatementParameter(2, getEmployeeId());
        con.setStatementParameter(3, venueName);
        con.setStatementParameter(4, venueCity);

        
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Insertion failed for: " + this);
        }
        con.close();
    }

    @Override
    public void update() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_EVENTMANAGERVENUE);
        con.setStatementParameter(1, venueName);
        con.setStatementParameter(2, venueCity);
        
        con.setStatementParameter(3, venueId);
        con.setStatementParameter(4, getEmployeeId());
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Update failed for: " + this);
        }
        con.close();
    }

    @Override
    public void delete() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(DELETE_EVENTMANAGER);
        con.setStatementParameter(1, venueId);
        con.setStatementParameter(2, getEmployeeId());
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();
    }
}
