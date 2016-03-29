/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_bean;

import database.active_record.Event;
import database.active_record.EventHandler;
import database.active_record.Transactions;
import database.active_record.TransactionHandler;
import database.active_record.User;
import database.active_record.UserHandler;
import database.active_record.UserTransaction;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Purushotham
 */

@Named(value = "user")
@SessionScoped
public class user_UI implements Serializable
{
    private int userId;
    private String name;
    private String username;
    private String password;
    private String defaultTransactionStatus;
    
    
    private final transient TransactionHandler transactionHandler;
    private final transient UserHandler userHandler;
    private final transient EventHandler eventHandler;
    private transient Event eventToView = null;
    private Event event;
    private UserTransaction userTrans;
    private transient Transactions transactionToCreate = null;
    private transient Transactions transactionToEdit = null;
    private transient Transactions transactionToView = null;
    private transient Transactions transactionToDelete = null;

    public user_UI()
    {
        clearFields();
        transactionHandler = new TransactionHandler();
        userHandler = new UserHandler();
        eventHandler = new EventHandler();
        
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String pwd)
    {
        this.password = pwd;
    }

    public void clearFields()
    {
        userId = -1;
        name = "";
        username = "";
        password = "";
       
    }

    public String checkCredentials()
    {
        String outcome = "";
        User sp = userHandler.findUserByUsername(username);

        if (sp != null)
        {
            if (sp.passwordIsCorrect(password))
            {
                userId = sp.getUserId();
                name = sp.getName();
                password = "";
                outcome = "loginOK";
            }
        }

        if (outcome.isEmpty())
        {
            clearFields();
            FacesMessage msg = new FacesMessage("Username and/or password not recognised.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        return outcome;
    }
    
    public String logout()
    {
            clearFields();
            return "logout";
    }
    
    // Transactions Creation Methods
    
     public ArrayList<Transactions> getAllTransactions()
    {
        return transactionHandler.findAllTransactions();
    }
    
    public Transactions getTransactionToCreate()
    {
        return transactionToCreate;
    }
    
    public String publishTransaction(Transactions s)
    {
        return transactionHandler.publishTransaction(s) ? "publishTransaction" : "";
    }

    public String setTransactionToCreate()
    {
        defaultTransactionStatus = "Pending";
        Date d = new Date(System.currentTimeMillis());
        transactionToCreate = new Transactions(defaultTransactionStatus, d, new Time(d.getTime()));
        return "bookTickets";
    }

    public String createTransaction()
    {
        String outcome = "";
        
        if (transactionHandler.createTransaction(transactionToCreate))
        {
            transactionToView = transactionToCreate;
            transactionToCreate = null;
            outcome = "transactionCreated";
        }
        return outcome;
    }

    public String cancelTransactionCreation()
    {
        transactionToCreate = null;
        return "cancel";
    }
    
    public Transactions getTransactionToView()
    {
        return transactionToView;
    }

    public String setTransactionToView(Transactions s)
    {
        transactionToView = s;
        return "transactionDetails";
    }
    
    public Transactions getTransactionToDelete()
    {
        return transactionToDelete;
    }

    public String setTransactionToDelete(Transactions s)
    {
        transactionToDelete = s;
        return "deleteTransaction";
    }

    public String deleteTransaction()
    {
        String outcome = "cancel";

        if (transactionHandler.deleteTransaction(transactionToDelete))
        {
            transactionToDelete = null;
            outcome = "transactionDeleted";
        }

        return outcome;
    }

    public String cancelTransactionDeletion()
    {
        transactionToDelete = null;
        return "cancel";
    }
    
    public Transactions getTransactionToEdit()
    {
        return transactionToEdit;
    }
    
    public String setTransactionToEdit()
    {
        transactionToEdit = transactionToView;
        return "editTransaction";
    }
    
    public String updateEditedTransaction()
    {
        String outcome = "";
        
        try
        {
            transactionToView.update();
            transactionToView = null;
            outcome = "transactionUpdated";
        }
        catch (Exception e)
        {
        }
        
        return outcome;
    }
    
    public String cancelTransactionEdit()
    {
            transactionToView = null;
            return "cancel";
    }
    
    public ArrayList<Event> getAllEvents()
    {
        return eventHandler.findAllEvents();
    }
    
    public Event getEvent()
    {
        return event;
    }

    public Event getEventToView()
    {
        return eventToView;
    }

    public String setEventToView(Event s)
    {
        eventToView = s;
        return "userEventDetails";
    }
    
    public String deleteUserFromTransaction(User person)
    {
        String outcome = "";

        if (transactionToView.deleteUser(person))
        {
            outcome = "deletePerson";
        }

        return outcome;
    }
   public ArrayList<UserTransaction> getUserTransactionsDetails()
    {
        return transactionHandler.findTransactionsForUser(userId);
    }
   
       
//
//    public Event getEventToDelete()
//    {
//        return eventToDelete;
//    }
//
//    public String setEventToDelete(Event s)
//    {
//        eventToDelete = s;
//        return "deleteEvent";
//    }
//
//    public String deleteEvent()
//    {
//        String outcome = "cancel";
//
//        if (eventHandler.deleteEvent(eventToDelete))
//        {
//            eventToDelete = null;
//            outcome = "eventDeleted";
//        }
//
//        return outcome;
//    }
//
//    public String cancelEventDeletion()
//    {
//        eventToDelete = null;
//        return "cancel";
//    }
//    
//    public String publishEvent(Event s)
//    {
//        return eventHandler.publishEvent(s) ? "publishVenue" : "";
//    }
//    
//    public Event getEventToEdit()
//    {
//        return eventToEdit;
//    }
//    
//    public String setEventToEdit()
//    {
//        eventToEdit = eventToView;
//        return "editEvent";
//    }
//    
//    public String updateEditedEvent()
//    {
//        String outcome = "";
//        
//        try
//        {
//            eventToView.update();
//            eventToView = null;
//            outcome = "eventUpdated";
//        }
//        catch (Exception e)
//        {
//        }
//        
//        return outcome;
//    }
//    
//    public String cancelEventEdit()
//    {
//            eventToView = null;
//            return "cancel";
//    }
}
