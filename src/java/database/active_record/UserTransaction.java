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
public class UserTransaction implements ActiveRecord
{
    private static final String INSERT_USERTRANSACTION = "INSERT INTO UserTransaction(UserId, TransactionId, EventId, TotalTickets, TotalCost) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USERTRANSACTION = "UPDATE UserTransaction " +
                                                            "SET TotalTickets=?, TotalCost=?" + 
                                                            "WHERE userId=? AND transactionId=? AND eventId=?";
    private static final String DELETE_USER = "DELETE FROM UserTransaction WHERE userId=? AND transactionId=?";

    private int transactionId;
    private int userId;
    private int eventId;
    private User person;
    private Event event;
    private int totalTickets;
    private int totalCost;
    
    

    public UserTransaction(int userId, int transactionId, int eventId)
    {
        this(-1,-1,-1,-1,-1); 
   }

    public UserTransaction(int userId, int transactionId, int eventId, int totalTickets, int totalCost)
    {
        this.transactionId = transactionId;
        this.userId = userId;
        this.eventId = eventId;
        this.totalTickets = totalTickets;
        this.totalCost = totalCost;
        
        
   }

    public int getTransactionId()
    {
        return transactionId;
    }

    public User getUser()
    {
        return person;
    }
    
     public Event getEvent()
    {
        return event;
    }

    public int getUserId()
    {
        return userId;
    }
    
    public int getEventId()
    {
        return eventId;
    }
    
    public String getUserName()
    {
        return person.getName();
    }
    
    public String getEventName()
    {
        return event.getEventName();
    }

    public int getTotalTickets()
    {
        return totalTickets;
    }

    public void setTotalTickets(int tt)
    {
        this.totalTickets = tt;
    }

    public int getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(int tc)
    {
        this.totalCost = tc;
    }
    @Override
    public String toString()
    {
        return getTransactionId() + ":" + getUserId() + ":" + getEventId() + ":" + getTotalTickets()+ ":" + getTotalCost();
    }

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_USERTRANSACTION);
        con.setStatementParameter(1, userId);
        con.setStatementParameter(2, transactionId);
        con.setStatementParameter(3, eventId);
        con.setStatementParameter(4, totalTickets);
        con.setStatementParameter(4, totalCost);
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
        con.prepareStatement(UPDATE_USERTRANSACTION);
        con.setStatementParameter(1, totalTickets);
        con.setStatementParameter(2, totalCost);
        con.setStatementParameter(3, transactionId);
        con.setStatementParameter(4, userId);
        con.setStatementParameter(5, eventId);
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
        con.prepareStatement(DELETE_USER);
        con.setStatementParameter(1, transactionId);
        con.setStatementParameter(2, userId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();
    }
}
