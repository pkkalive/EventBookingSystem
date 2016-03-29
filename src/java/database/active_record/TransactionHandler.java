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
public class TransactionHandler {
    private static final String FIND_ALL_TRANSACTIONS = "SELECT * FROM Transactions";
    private static final String FIND_TRANSACTION_BY_ID = "SELECT * FROM Transactions WHERE TransactionId=?";
    private static final String FIND_TRANSACTIONS_BY_DATE = "SELECT * FROM Transactions WHERE TransactionDate=?";
    private static final String FIND_TRANSACTIONS_FOR_USER = "SELECT Users.*, UserTransaction.TotalTickets, UserTransaction.TotalCost "
            + "FROM UserTransaction WHERE Users.UsersId=UserTransaction.UsersId";
    private static final String FIND_USERS_NOT_IN_TRANSACTION = 
            "SELECT Users.* " + 
            "FROM Users " + 
            "WHERE UserId NOT IN (SELECT DISTINCT UserId FROM UserTransaction WHERE TransactionId=?)";
    
    public boolean createTransaction(Transactions s)
    {
        boolean transactionCreated = false;
        ArrayList<Transactions> transactions = findTransactionsByDate(s.getTransactionDate());
        boolean found = false;
        
        for (Transactions transaction: transactions)
        {
            if (transaction.getTransactionTime().equals(s.getTransactionTime()))
            {
                found = true;
            }
        }
        
        if (!found)
        {
            try
            {
                s.insert();
                transactionCreated = true;
            }
            catch (Exception e)
            {
            }
        }
        return transactionCreated;
    }
    
    public boolean publishTransaction(Transactions transaction)
    {
        boolean transactionPublished = false;
        
        if (transaction != null)
        {
            transaction.setPublished(true);
            try
            {
                transaction.update();
                transactionPublished = true;
            }
            catch (Exception e)
            {
            }
        }
        return transactionPublished;
    }

    

    public boolean deleteTransaction(Transactions transaction)
    {
        boolean transactionDeleted = false;
        
        if (transaction != null)
        {
            try
            {
                transaction.delete();
                transactionDeleted = true;
            }
            catch (Exception e)
            {
            }
        }
        return transactionDeleted;
    }

//    private Transaction prepareTransaction(ResultSet rs) throws SQLException
//    {
//        TransactionManager p = new TransactionManager(rs.getInt("EmpId"), rs.getString("Name"), rs.getString("Username"), rs.getString("Password"));
//        return new Transaction(rs.getInt("TransactionId"),rs.getBoolean("IsPublished"), rs.getString("VeueName"), rs.getDate("TransactionDate"),
//                                  rs.getTime("StartTime"),
//                                  rs.getTime("EndTime"),
//                                  rs.getInt("Cost"));
//    }

    private Transactions prepareTransaction(ResultSet rs)
    {
        Transactions transaction = null;

        try
        {
            if (rs.next())
            {
                transaction = new Transactions(rs.getInt("TransactionId"),
                                               rs.getBoolean("IsPublished"),          
                                               rs.getString("TransactionStatus"),
                                               rs.getDate("TransactionDate"),
                                               rs.getTime("TransactionTime"));
            }
        }
        catch (SQLException sqle)
        {
        }

        return transaction;
    }

    private ArrayList<Transactions> prepareTransactions(ResultSet rs)
    {
        ArrayList<Transactions> list = new ArrayList();

        try
        {
            if (rs.next())
            {
                int transactionId = rs.getInt("TransactionId");
                Transactions transaction = new Transactions(transactionId,
                                               rs.getBoolean("IsPublished"),          
                                               rs.getString("TransactionStatus"),
                                               rs.getDate("TransactionDate"),
                                               rs.getTime("TransactionTime"));
             
                list.add(transaction);
                while (rs.next())
                {
                    int nextTransactionId = rs.getInt("TransactionId");
                    if (nextTransactionId != transactionId)
                    {
                        transactionId = nextTransactionId;
                        transaction = new Transactions(transactionId,
                                               rs.getBoolean("IsPublished"),          
                                               rs.getString("TransactionStatus"),
                                               rs.getDate("TransactionDate"),
                                               rs.getTime("TransactionTime"));

                        list.add(transaction);
                    }
                   
                }
            }
        }
        catch (SQLException sqle)
        {
        }

        return list;
    }
    
    private ArrayList<UserTransaction> prepareUserTransaction(ResultSet rs)
    {
        ArrayList<UserTransaction> list = new ArrayList();

        try
        {
            if (rs.next())
            {
                int userId = rs.getInt("UserId");
                UserTransaction userTransaction = new UserTransaction(userId,
                                               rs.getInt("TransactionId"),          
                                               rs.getInt("EventId"),
                                               rs.getInt("TotalTickets"),
                                               rs.getInt("TotalCost"));
             
                list.add(userTransaction);
                while (rs.next())
                {
                    int nextUserId = rs.getInt("UserId");
                    if (nextUserId != userId)
                    {
                        userId = nextUserId;
                        userTransaction = new UserTransaction(userId,
                                               rs.getInt("TransactionId"),          
                                               rs.getInt("EventId"),
                                               rs.getInt("TotalTickets"),
                                               rs.getInt("TotalCost"));

                        list.add(userTransaction);
                    }
                   
                }
            }
        }
        catch (SQLException sqle)
        {
        }

        return list;
    }

    public ArrayList<Transactions> findAllTransactions()
    {
        ArrayList<Transactions> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_ALL_TRANSACTIONS);
        if (con.executePreparedStatement())
        {
            list = prepareTransactions(con.getResultSet());
        }
        con.close();

        return list;
    }
    
    public Transactions findTransactionById(int eventId)
    {
        Transactions sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_TRANSACTION_BY_ID);
        con.setStatementParameter(1, eventId);
        if (con.executePreparedStatement())
        {
            sp = prepareTransaction(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<Transactions> findTransactionsByDate(Date date)
    {
        ArrayList<Transactions> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_TRANSACTIONS_BY_DATE);
        con.setStatementParameter(1, date);
        if (con.executePreparedStatement())
        {
            list = prepareTransactions(con.getResultSet());
        }
        con.close();

        return list;
    }

    public ArrayList<UserTransaction> findTransactionsForUser(int userId)
    {
        ArrayList<UserTransaction> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_TRANSACTIONS_FOR_USER);
        con.setStatementParameter(1, userId);
        if (con.executePreparedStatement())
        {
            list = prepareUserTransaction(con.getResultSet());
        }
        con.close();

        return list;
    }
}
