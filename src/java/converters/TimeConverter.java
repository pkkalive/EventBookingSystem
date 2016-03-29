/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@FacesConverter("timeConverter")
public class TimeConverter implements Converter
{

    private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public TimeConverter()
    {
        sdf.setLenient(false);
    }

    @Override
    public Object getAsObject(FacesContext context,
            UIComponent component,
            String newValue) throws ConverterException
    {
        if (newValue.isEmpty())
        {
            return newValue;
        }
        
        try
        {
            Date d = sdf.parse(newValue);
            return new Time(d.getTime());
        }
        catch (ParseException e)
        {
            String msgDetail = "Error: '" + newValue + "' is not a time of the form <HH:mm>";
            FacesMessage msg = new FacesMessage("Invalid time format",
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

        if (value instanceof Time)
        {
            return sdf.format((Time) value);
        }

        String msgDetail = "Unexpected type " + value.getClass().getName();
        FacesMessage msg = new FacesMessage("Time conversion error",
                msgDetail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        throw new ConverterException(msg);
    }
}
