/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertor;

import entity.Facility;
import entity.KeyValue;
import entity.Members;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ahas36
 */
@FacesConverter("convertor.facility")
public class FacilityConvertor implements Converter {

    @PersistenceContext(unitName = "BookingSystemPU")
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Facility f=null;
        try
        {
        f= em.find(Facility.class, Integer.valueOf(value));
        }
        catch(Exception e)
        {
            FacesMessage msg
                    = new FacesMessage("Member cannot be found",
                            "Invalid member id. Member id should be an integer");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        if(f==null)
        {
            FacesMessage msg
                    = new FacesMessage("Member cannot be found",
                            "No member exsist with id "+value);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        return f;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Facility find = em.find(Facility.class, ((KeyValue)value).getKey());
        return find.getFacilityId().toString();
    }

}
