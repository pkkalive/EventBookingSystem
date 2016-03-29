/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_bean;

import database.active_record.EmployeeHandler;
import database.active_record.Event;
import database.active_record.EventHandler;
import database.active_record.EventManager;
import database.active_record.Venue;
import database.active_record.VenueHandler;

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
@Named(value = "eventManager")
@SessionScoped
public class eventmanager_UI implements Serializable
{
    private int empId;
    private String name;
    private String username;
    private String password;
    private String defaultEventName;
    private int defaultCost;

    private final transient VenueHandler venueHandler;
    private final transient EventHandler eventHandler;
    private final transient EmployeeHandler employeeHandler;
    private transient Venue venueToEdit = null;
    private transient Event eventToEdit = null;
    private transient Event eventToCreate = null;
    private transient Event eventToView = null;
    private transient Event eventToDelete = null;

    public eventmanager_UI()
    {
        clearFields();

        venueHandler = new VenueHandler();
        employeeHandler = new EmployeeHandler();
        eventHandler = new EventHandler();
    }

    public int getEmpId()
    {
        return empId;
    }

    public void setEmpId(int empId)
    {
        this.empId = empId;
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
        empId = -1;
        name = "";
        username = "";
        password = "";
        venueToEdit = null;
    }

    public String checkCredentials()
    {
        String outcome = "";
        EventManager sp = employeeHandler.findEmployeeByUsername(username);

        if (sp != null)
        {
            if (sp.passwordIsCorrect(password))
            {
                empId = sp.getEmployeeId();
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
    
    public ArrayList<Venue> getEmployeeVenues()
    {
        return venueHandler.findVenuesForEventManager(empId);
    }
    
    public Venue getVenueToEdit()
    {
        return venueToEdit;
    }
    
    public String setVenueToEdit(Venue s)
    {
        venueToEdit = s;
        return "edit";
    }
    
    public String updateEditedVenue()
    {
        String outcome = "";
        
        try
        {
            venueToEdit.update();
            venueToEdit = null;
            outcome = "myEventUpdated";
        }
        catch (Exception e)
        {
        }
        
        return outcome;
    }
    
    public String cancelVenueEdit()
    {
            venueToEdit = null;
            return "cancel";
    }
    
    public String logout()
    {
            clearFields();
            return "logout";
    }
    
    // Event Creation Methods
    
     public ArrayList<Event> getAllEvents()
    {
        return eventHandler.findAllEvents();
    }
    
    public Event getEventToCreate()
    {
        return eventToCreate;
    }

    public String setEventToCreate()
    {
        defaultEventName = "World Film Festival";
        defaultCost = 5;
        Date d = new Date(System.currentTimeMillis());
        eventToCreate = new Event(defaultEventName, d, new Time(d.getTime()), new Time(d.getTime()+28800000), defaultCost);
        return "createEvent";
    }

    public String createEvent()
    {
        String outcome = "";
        
        if (eventHandler.createEvent(eventToCreate))
        {
            eventToView = eventToCreate;
            eventToCreate = null;
            outcome = "eventCreated";
        }
        return outcome;
    }

    public String cancelEventCreation()
    {
        eventToCreate = null;
        return "cancel";
    }

    public Event getEventToView()
    {
        return eventToView;
    }

    public String setEventToView(Event s)
    {
        eventToView = s;
        return "eventDetails";
    }

    public Event getEventToDelete()
    {
        return eventToDelete;
    }

    public String setEventToDelete(Event s)
    {
        eventToDelete = s;
        return "deleteEvent";
    }

    public String deleteEvent()
    {
        String outcome = "cancel";

        if (eventHandler.deleteEvent(eventToDelete))
        {
            eventToDelete = null;
            outcome = "eventDeleted";
        }

        return outcome;
    }

    public String cancelEventDeletion()
    {
        eventToDelete = null;
        return "cancel";
    }
    
    public String publishEvent(Event s)
    {
        return eventHandler.publishEvent(s) ? "publishVenue" : "";
    }
    
    public Event getEventToEdit()
    {
        return eventToEdit;
    }
    
    public String setEventToEdit()
    {
        eventToEdit = eventToView;
        return "editEvent";
    }
    
    public String updateEditedEvent()
    {
        String outcome = "";
        
        try
        {
            eventToView.update();
            eventToView = null;
            outcome = "eventUpdated";
        }
        catch (Exception e)
        {
        }
        
        return outcome;
    }
    
    public String cancelEventEdit()
    {
            eventToView = null;
            return "cancel";
    }
}
