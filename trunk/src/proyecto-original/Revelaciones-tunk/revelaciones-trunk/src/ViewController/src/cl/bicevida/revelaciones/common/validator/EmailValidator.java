package cl.bicevida.revelaciones.common.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class EmailValidator implements Validator {
    
    public EmailValidator() {
        super();
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {            
        String email = (String)object;                
        org.apache.commons.validator.routines.EmailValidator validator = org.apache.commons.validator.routines.EmailValidator.getInstance();        
        if (!validator.isValid(email)) {
            FacesMessage message = new FacesMessage();
            message.setDetail("La dirección de email ingresada no es válida");
            message.setSummary("Email no válido");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
