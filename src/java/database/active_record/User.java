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
import java.util.Objects;

/**
 *
 * @author Purushotham
 */
public class User implements ActiveRecord
{

    private static final String GET_USER_ID = "SELECT UserId FROM Users WHERE UserName=?";
    private static final String INSERT_USER = "INSERT INTO Users(Name, UserName, Password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE User " 
                                               + "SET Name=?, UserName=?, Password=? "
                                               + "WHERE userId=?";
    private static final String DELETE_USER = "DELETE FROM User WHERE userId=?";

    private int userId;
    private String name;
    private String username;
    private String password;

    public User(int userId, String name, String username, String password)
    {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String pwd)
    {
        this.password = pwd;
    }

    public boolean passwordIsCorrect(String pwd)
    {
        return password.equals(pwd);
    }

    @Override
    public String toString()
    {
        return userId + ":" + name + ":" + username;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof User))
        {
            return false;
        }
        
        if (obj == this)
        {
            return true;
        }
        
        User sp = (User)obj;
        return (this.userId == sp.userId &&
                this.name.equals(sp.getName()) &&
                this.username.equals(sp.getUsername()));
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 67 * hash + this.userId;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.username);
        return hash;
    }
    
    

    @Override
    public void insert() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(INSERT_USER);
        con.setStatementParameter(1, name);
        con.setStatementParameter(2, username);
        con.setStatementParameter(3, password);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Insertion failed for: " + this);
        }
        con.prepareStatement(GET_USER_ID);
        con.setStatementParameter(1, username);
        if (con.executePreparedStatement())
        {
            try
            {
                ResultSet rs = con.getResultSet();
                userId = rs.getInt("UserId");
            }
            catch (SQLException sqle)
            {

            }
        }
        con.close();
    }

    @Override
    public void update() throws Exception
    {
        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(UPDATE_USER);
        con.setStatementParameter(1, name);
        con.setStatementParameter(2, username);
        con.setStatementParameter(3, password);
        con.setStatementParameter(4, userId);
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
        con.setStatementParameter(1, userId);
        if (!con.executePreparedStatement()
                && con.getUpdateCount() < 1)
        {
            throw new Exception("Deletion failed for: " + this);
        }
        con.close();
    }
}


