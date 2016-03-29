/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
@FacesConverter("dateConverter")
public class DateConverter implements Converter
{
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    public DateConverter()
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
            return new Date(sdf.parse(newValue).getTime());
        }
        catch (Exception e)
        {
            String msgDetail = "Error: '" + newValue + "' is not a date of the form <dd-MMM-yyyy>";
            FacesMessage msg = new FacesMessage("Invalid date format",
                    msgDetail);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ConverterException(e);
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

        if (value instanceof Date)
        {
            return sdf.format((Date)value);
        }

        String msgDetail = "Unexpected type " + value.getClass().getName();
        FacesMessage msg = new FacesMessage("Date conversion error",
                msgDetail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        throw new ConverterException(msg);
    }
}

