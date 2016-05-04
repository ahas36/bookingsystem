/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertor;

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
@FacesConverter("convertor.member")
public class MemberConvertor implements Converter {

    @PersistenceContext(unitName = "BookingSystemPU")
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Members m=null;
        try
        {
        m= em.find(Members.class, Integer.valueOf(value));
        }
        catch(Exception e)
        {
            FacesMessage msg
                    = new FacesMessage("Member cannot be found",
                            "Invalid member id. Member id should be an integer");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        if(m==null)
        {
            FacesMessage msg
                    = new FacesMessage("Member cannot be found",
                            "No member exsist with id "+value);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        return m;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Members) value).getMemberId().toString(); 
    }

}
