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
public class UserHandler {
    
    private static final String FIND_USER_BY_ID = "SELECT * FROM Users WHERE UserId=?";
    private static final String FIND_USER_BY_NAME = "SELECT * FROM Users WHERE Name=?";
    private static final String FIND_USER_BY_USERNAME = "SELECT * FROM Users WHERE UserName=?";
    

    private User prepareUser(ResultSet rs)
    {
        User sp = null;

        try
        {
            if (rs.next())
            {
                sp = new User(rs.getInt("UserId"),
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

    private ArrayList<User> prepareUserPeople(ResultSet rs)
    {
        ArrayList<User> list = new ArrayList();

        try
        {
            while (rs.next())
            {
                list.add(new User(rs.getInt("UserId"),
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

    public User findUserById(int userId)
    {
        User sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_USER_BY_ID);
        con.setStatementParameter(1, userId);
        if (con.executePreparedStatement())
        {
            sp = prepareUser(con.getResultSet());
        }
        con.close();

        return sp;
    }

    public ArrayList<User> findUsersByName(String name)
    {
        ArrayList<User> list = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_USER_BY_NAME);
        con.setStatementParameter(1, name);
        if (con.executePreparedStatement())
        {
            list = prepareUserPeople(con.getResultSet());
        }
        con.close();

        return list;
    }

    public User findUserByUsername(String username)
    {
        User sp = null;

        DatabaseConnection con = ConnectionManager.getInstance().getConnection();
        con.prepareStatement(FIND_USER_BY_USERNAME);
        con.setStatementParameter(1, username);
        if (con.executePreparedStatement())
        {
            sp = prepareUser(con.getResultSet());
        }
        con.close();

        return sp;
    }

    
    
}

