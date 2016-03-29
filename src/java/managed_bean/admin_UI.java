/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managed_bean;

import database.active_record.EmployeeHandler;
import database.active_record.EventManager;
import database.active_record.Venue;
import database.active_record.VenueHandler;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Purushotham
 */

@Named(value = "admin")
@SessionScoped

public class admin_UI implements Serializable
{

    private String username;
    private String password;
    private String defaultVenueName;
    private String defaultVenueCity;

    private final transient VenueHandler venueHandler;
    private final transient EmployeeHandler employeeHandler;
    private transient Venue venueToCreate = null;
    private transient Venue venueToEdit = null;
    private transient Venue venueToView = null;
    private transient Venue venueToDelete = null;
    private EventManager selectedPerson = null;

    public admin_UI()
    {
        clearFields();

        venueHandler = new VenueHandler();
        employeeHandler = new EmployeeHandler();
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
        username = "";
        password = "";
        venueToView = null;
        venueToDelete = null;
        selectedPerson = null;
    }

    public String checkCredentials() 
    {
        String outcome = "";

        if (username.equalsIgnoreCase("admin")
                && password.equals("Admin"))
        {
            password = "";
            outcome = "loginOK";
        }

        if (outcome.isEmpty())
        {
            clearFields();
            FacesMessage msg = new FacesMessage("Username and/or password not recognised.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        return outcome;
    }

    public ArrayList<Venue> getAllVenues() 
    {
        return venueHandler.findAllVenues();
    }

    public Venue getVenueToCreate() 
    {
        return venueToCreate;
    }

    public String setVenueToCreate()
    {
        defaultVenueName = "Venue Name";
        defaultVenueCity = "Vancouver";
        
        venueToCreate = new Venue(defaultVenueName, defaultVenueCity);
        return "createVenue";
    }

    public String createVenue()
    {
        String outcome = "";
        
        if (venueHandler.createVenue(venueToCreate))
        {
            venueToView = venueToCreate;
            venueToCreate = null;
            outcome = "venueCreated";
        }
        return outcome;
    }

    public String cancelVenueCreation()
    {
        venueToCreate = null;
        return "cancel";
    }

    public Venue getVenueToView()
    {
        return venueToView;
    }

    public String setVenueToView(Venue s)
    {
        venueToView = s;
        return "venueDetails";
    }

    public Venue getVenueToDelete()
    {
        return venueToDelete;
    }

    public String setVenueToDelete(Venue s)
    {
        venueToDelete = s;
        return "deleteVenue";
    }

    public String deleteVenue()
    {
        String outcome = "cancel";

        if (venueHandler.deleteVenue(venueToDelete))
        {
            venueToDelete = null;
            outcome = "venueDeleted";
        }

        return outcome;
    }

    public String cancelVenueDeletion()
    {
        venueToDelete = null;
        return "cancel";
    }

    public String deletePersonFromVenue(EventManager person)
    {
        String outcome = "";

        if (venueToView.deleteEventManager(person))
        {
            outcome = "deletePerson";
        }

        return outcome;
    }

    public String publishVenue(Venue s)
    {
        return venueHandler.publishVenue(s) ? "publishVenue" : "";
    }
    
    public ArrayList<EventManager> getPeopleAvailableForVenue()
    {
        return employeeHandler.findEmployeesNotInVenue(venueToView.getVenueId());
    }

    public EventManager getSelectedPerson()
    {
        return selectedPerson;
    }

    public void setSelectedPerson(EventManager selectedPerson)
    {
        this.selectedPerson = selectedPerson;
    }
    
    public String addSelectedPerson()
    {
        String outcome = "";
        
        if (venueToView.addEventManager(selectedPerson))
        {
            selectedPerson = null;
            outcome = "personAdded";
        }
        return outcome;
    }

    public String cancelPersonSelection()
    {
        selectedPerson = null;
        return "cancel";
    }

    public String logout() 
    {
        clearFields();
        return "logout";
    }
    
    public Venue getVenueToEdit()
    {
        return venueToEdit;
    }
    
    public String setVenueToEdit()
    {
        venueToEdit = venueToView;
        return "editVenue";
    }
    
    public String updateEditedVenue() 
    {
        String outcome = "";
        
        try
        {
            venueToView.venueUpdate();
            venueToView = null;
            outcome = "venueUpdated";
        }
        catch (Exception e)
        {
        }
        
        return outcome;
    }
    
    public String cancelVenueEdit() 
    {
            venueToView = null;
            return "cancel";
    }
}
