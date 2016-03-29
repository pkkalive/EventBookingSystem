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
public class EmployeeHandler 
{
    
    private static final String FIND_EMPLOYEE_BY_ID = "SELECT * FROM EventManager WHERE EmpId=?";
    private static final String FIND_EMPLOYEE_BY_NAME = "SELECT * FROM EventManager WHERE Name=?";
    private static final String FIND_EMPLOYEE_BY_USERNAME = "SELECT * FROM EventManager WHERE UserName=?";
    private static final String FIND_EMPLOYEES_NOT_IN_VENUE = 
            "SELECT EventManager.* " + 
            "FROM EventManager " + 
            "WHERE EmpId NOT IN (SELECT DISTINCT EmpId FROM EventManagerVenue WHERE VenueId=?)";

    private EventManager prepareEventManager(ResultSet rs)
    {
        EventManager sp = null;

        try
        {
            if (rs.next())
            {
                sp = new EventManager(rs.getInt("EmpId"),
                        rs.getString("Name"),
                        rs.getString("UserName"),
                        rs.getString("Password"));
            }
        }
        catch (SQLException sqle)
        {
        }

        return sp;
    }

    private ArrayList<EventManager> prepareEventPeople(ResultSet rs)
    {
        ArrayList<EventManager> list = new ArrayList();

        try
        {
            while (rs.next())
            {
                list.add(new EventManager(rs.getInt("EmpId"),
                        rs.getString("Name"),
                        rs.getString("UserName"),
                        rs.getString("Password")));
            }
        }
        catch (SQLException sqle)
        {
        }

        return list;
    }

    public EventManager findEmployeeById(int empId)
    {
        EventManager sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EMPLOYEE_BY_ID);
        con.setStatementParameter(1, empId);
        if (con.executePreparedStatement())
        {
            sp = prepareEventManager(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<EventManager> findEmployeesByName(String name)
    {
        ArrayList<EventManager> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EMPLOYEE_BY_NAME);
        con.setStatementParameter(1, name);
        if (con.executePreparedStatement())
        {
            list = prepareEventPeople(con.getResultSet());
        }
        con.close();

        return list;
    }

    public EventManager findEmployeeByUsername(String username)
    {
        EventManager sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EMPLOYEE_BY_USERNAME);
        con.setStatementParameter(1, username);
        if (con.executePreparedStatement())
        {
            sp = prepareEventManager(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<EventManager> findEmployeesNotInVenue(int venueId)
    {
        ArrayList<EventManager> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_EMPLOYEES_NOT_IN_VENUE);
        con.setStatementParameter(1, venueId);
        if (con.executePreparedStatement())
        {
            list = prepareEventPeople(con.getResultSet());
        }
        con.close();

        return list;
    }
    
}
