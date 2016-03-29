/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import database.active_record.EmployeeHandler;
import database.active_record.EventManager;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Purushotham
 */
@FacesConverter("eventManagerConverter")
public class EventManagerConverter implements Converter
{

    private static final transient EmployeeHandler employeeHandler = new EmployeeHandler();

    @Override
    public Object getAsObject(FacesContext context,
            UIComponent component,
            String newValue) throws ConverterException
    {
        if (newValue.isEmpty())
        {
            return newValue;
        }

        String[] parts = newValue.split(":");

        try
        {
            int empId = Integer.parseInt(parts[0]);
            return employeeHandler.findEmployeeById(empId);
        }
        catch (NumberFormatException nfe)
        {
            String msgDetail = "Error converting the selected person";
            FacesMessage msg = new FacesMessage("EventManager conversion error",
                    msgDetail);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ConverterException(msg);
        }
    }

    @Override
    public String getAsString(FacesContext context,
            UIComponent component,
            Object value) throws ConverterException
    {
        if (value == null)
        {
            return "";
        }

        if (value instanceof EventManager)
        {
            return value.toString();
        }

        String msgDetail = "Unexpected type " + value.getClass().getName();
        FacesMessage msg = new FacesMessage("EventManager conversion error",
                msgDetail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        throw new ConverterException(msg);
    }
}
