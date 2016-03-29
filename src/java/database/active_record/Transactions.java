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
public class Transactions implements ActiveRecord
{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat stf = new SimpleDateFormat("hh:mm");

    private static final String GET_TRANSACTION_ID = "SELECT TransactionId FROM Transactions WHERE TransactionStatus=? AND TransactionDate=? AND TransactionTime=?";
    private static final String INSERT_TRANSACTION = "INSERT INTO Transactions(IsPublished, TransactionStatus, TransactionDate, TransactionTime) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TRANSACTION
            = "UPDATE Transactions "
            + "SET IsPublished=?, TransactionStatus=?, TransactionDate=?, TransactionTime=?"
            + "WHERE TransactionId=?";
    private static final String DELETE_TRANSACTION = "DELETE FROM Transactions WHERE TransactionId=?";

    private int transactionId;
    private boolean published;
    private String transactionStatus;
    private Date transactionDate;
    private Time transactionTime;
    private int totalTickets;
    private int totalCost;
    private User person;
    private Event event;
    

    private HashMap<Integer, UserTransaction> userTransaction;
    private HashMap<Integer, Transactions> transactionDetails;

    public Transactions(String transactionStatus, Date transactionDate, Time transactionTime)
    {
        this(-1, false, transactionStatus, transactionDate, transactionTime);
    }

    public Transactions(int transactionId, boolean published, String transactionStatus, Date transactionDate, Time transactionTime)
    {
        this.transactionId = transactionId;
        this.published = published;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        
        
      userTransaction = new HashMap<>();
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public boolean isPublished()
    {
        return published;
    }

    public void setPublished(boolean b)
    {
        this.published = b;
    }

    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus()
    {
        this.transactionStatus = "Pending";
    } 
    
    public Date getTransactionDate()
    {
        return transactionDate; 
    }

    public void setTransactionDate(Date d)
    {
        this.transactionDate = d;
    }

    public Time getTransactionTime()
    {
        return transactionTime;
    }

    public void setTransactionTime(Time t)
    {
        this.transactionTime = t;
    }
    
    public Transactions getTransactionDetails(int transactionId)
    {
        return transactionDetails.get(transactionId);
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
        return person.getUserId();
    }
    
    public int getEventId()
    {
        return event.getEventId();
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
        return transactionId + ":"
                + (published ? "published" : "unpublished") + ":"
                + transactionStatus + ":"
                + sdf.format(transactionDate) + ":"
                + stf.format(transactionTime) + ":"
                + totalTickets + ":"
                + totalCost;
    }

    public boolean addUserTransaction(UserTransaction ps)
    {
        boolean userTransactionAdded = false;

        if (!userTransaction.containsKey(ps.getUser().getUserId()))
        {
            userTransaction.put(ps.getUser().getUserId(), ps);
            userTransactionAdded = true;
        }

        return userTransactionAdded;
    }
    
    public boolean deleteUser(User p)
    {
        boolean personAdded = false;

        if (userTransaction.containsKey(p.getUserId()))
        {
            UserTransaction ps = userTransaction.get(p.getUserId());
            try
            {
                ps.delete();
                userTransaction.remove(p.getUserId());
                personAdded = true;
            }
            catch (Exception e)
            {
            }
        }

        return personAdded;
    }

//    public boolean addSalesPerson(TransactionManager p)
//    {
//        boolean personAdded = false;
//
//        if (!personTransactions.containsKey(p.getEmployeeId()))
//        {
//            PersonTransaction ps = new PersonTransaction(eventId, p);
//            try
//            {
//                ps.insert();
//                personTransactions.put(p.getEmployeeId(), ps);
//                personAdded = true;
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        return personAdded;
//    }

//    public boolean deleteSalesPerson(TransactionManager p)
//    {
//        boolean personDeleted = false;
//
//        if (personTransactions.containsKey(p.getEmployeeId()))
//        {
//            PersonTransaction ps = personTransactions.get(p.getEmployeeId());
//            try
//            {
//                ps.delete();
//                personTransactions.remove(p.getEmployeeId());
//                personDeleted = true;
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        return personDeleted;
//    }

    public HashMap<Integer, UserTransaction> getUserTransactions()
    {
        return userTransaction;
    }
    
    public UserTransaction[] getUserTransactionsAsArray()
    {
        return userTransaction.values().toArray(new UserTransaction[0]);
    }
    
    public UserTransaction getUserTransaction(int userId)
    {
        return userTransaction.get(userId);
    }

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_TRANSACTION);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, transactionStatus);
        con.setStatementParameter(3, transactionDate);
        con.setStatementParameter(4, transactionTime);
        
       
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Insertion failed for: " + this);
        }
        con.prepareStatement(GET_TRANSACTION_ID);
        con.setStatementParameter(1, transactionStatus);
        con.setStatementParameter(2, transactionDate);
        con.setStatementParameter(3, transactionTime);
        
        
        if (con.executePreparedStatement())
        {
            try
            {
                ResultSet rs = con.getResultSet();
                if (rs.next())
                {
                    transactionId = rs.getInt("TransactionId");
                }
            }
            catch (SQLException sqle)
            {

            }
        }
        for (Integer key : userTransaction.keySet())
        {
            userTransaction.get(key).insert();
        }
        con.close();
    }

    @Override
    public void update() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_TRANSACTION);
        con.setStatementParameter(1, published);
        con.setStatementParameter(2, transactionStatus);
        con.setStatementParameter(3, transactionDate);
        con.setStatementParameter(4, transactionTime);
        con.setStatementParameter(5, transactionId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Update failed for: " + this);
        }
        for (Integer key : userTransaction.keySet())
        {
            userTransaction.get(key).update();
        }
        con.close();
    }

    @Override
    public void delete() throws Exception
    {
        //relies on the database cascading the deletion to the Transactions records
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(DELETE_TRANSACTION);
        con.setStatementParameter(1, transactionId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();

        userTransaction = null;
    }
}