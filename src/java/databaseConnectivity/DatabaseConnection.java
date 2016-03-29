/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseConnectivity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 *
 * @author Purushotham
 */
public class DatabaseConnection {
    
    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/FestivalEventBookingSystem;user=FestivalEvent;password=Event1";

    private final ConnectionManager connectionManager;
    private Connection connection = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private int updateCount = -1;

    DatabaseConnection(ConnectionManager connectionManager) throws SQLException
    {
        this.connectionManager = connectionManager;

        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        connection = DriverManager.getConnection(DATABASE_URL);
    }

    public void close()
    {
        connectionManager.releaseConnection(this);
    }

    public boolean prepareStatement(String sql)
    {
        boolean statementPrepared = false;

        try
        {
            stmt = connection.prepareStatement(sql);
            statementPrepared = true;
        }
        catch (SQLException sqle)
        {
        }

        return statementPrepared;
    }

    public boolean setStatementParameter(int paramIndex, int paramValue)
    {
        boolean paramSet = false;

        if (stmt != null)
        {
            try
            {
                stmt.setInt(paramIndex, paramValue);
                paramSet = true;
            }
            catch (SQLException sqle)
            {
            }
        }
        return paramSet;
    }

    public boolean setStatementParameter(int paramIndex, String paramValue)
    {
        boolean paramSet = false;

        if (stmt != null)
        {
            try
            {
                stmt.setString(paramIndex, paramValue);
                paramSet = true;
            }
            catch (SQLException sqle)
            {
            }
        }
        return paramSet;
    }

    public boolean setStatementParameter(int paramIndex, boolean paramValue)
    {
        boolean paramSet = false;

        if (stmt != null)
        {
            try
            {
                stmt.setBoolean(paramIndex, paramValue);
                paramSet = true;
            }
            catch (SQLException sqle)
            {
            }
        }
        return paramSet;
    }

    public boolean setStatementParameter(int paramIndex, Date paramValue)
    {
        boolean paramSet = false;

        if (stmt != null)
        {
            try
            {
                stmt.setDate(paramIndex, paramValue);
                paramSet = true;
            }
            catch (SQLException sqle)
            {
            }
        }
        return paramSet;
    }

    public boolean setStatementParameter(int paramIndex, Time paramValue)
    {
        boolean paramSet = false;

        if (stmt != null)
        {
            try
            {
                stmt.setTime(paramIndex, paramValue);
                paramSet = true;
            }
            catch (SQLException sqle)
            {
            }
        }
        return paramSet;
    }

    public boolean executePreparedStatement()
    {
        boolean resultSetAvailable = false;

        try
        {
            resultSetAvailable = stmt.execute();
            updateCount = stmt.getUpdateCount();
            rs = stmt.getResultSet();
        }
        catch (SQLException sqle)
        {
        }

        return resultSetAvailable;
    }

    public int getUpdateCount()
    {
        return updateCount;
    }

    public ResultSet getResultSet()
    {
        return rs;
    }
    
}
