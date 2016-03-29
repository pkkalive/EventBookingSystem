/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseConnectivity;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Purushotham
 */
public class ConnectionManager {
    private static final ConnectionManager instance = new ConnectionManager();

    private final ArrayList<DatabaseConnection> availableConnections;
    private final ArrayList<DatabaseConnection> busyConnections;

    private ConnectionManager()
    {
        busyConnections = new ArrayList();
        availableConnections = new ArrayList();

        for (int i = 0; i < 5; i++)
        {
            try
            {
                availableConnections.add(new DatabaseConnection(this));
            }
            catch (SQLException sqle)
            {
            }
        }
    }

    public static ConnectionManager getInstance()
    {
        return instance;
    }

    public DatabaseConnection getConnection()
    {
        DatabaseConnection connection = null;
        synchronized (this)
        {
            if (!availableConnections.isEmpty())
            {
                connection = availableConnections.remove(0);
                busyConnections.add(connection);
            }
        }
        return connection;
    }

    public void releaseConnection(DatabaseConnection connection)
    {
        if (connection != null)
        {
            synchronized (this)
            {
                if (busyConnections.remove(connection))
                {
                    availableConnections.add(connection);
                }
            }
        }
    }
    
}
