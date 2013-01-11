package cl.bicevida.revelaciones.common.validator;

import cl.bicevida.revelaciones.ejb.cross.Util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class RutValidator implements Validator {
    public RutValidator() {
        super();
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
        String rut = (String)object;                                        
        if (!Util.validaRutRegExp(rut)) {
            FacesMessage message = new FacesMessage();
            message.setDetail("El formato del Rut es inválido, debe ingresar el siguiente formato: 99999999-9");
            message.setSummary("Rut no válido");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
        String rutArray[] = rut.split("-");
        int rutInt = Util.getInteger(rutArray[0], 0);
        char dv = (char)rutArray[1].charAt(0);
        if(!Util.validarRut(rutInt, dv)){
            FacesMessage message = new FacesMessage();
            message.setDetail("El dígito verificador del Rut es incorrecto");
            message.setSummary("Rut no válido");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
