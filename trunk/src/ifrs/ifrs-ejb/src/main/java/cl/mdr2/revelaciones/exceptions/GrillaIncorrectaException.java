package cl.bicevida.revelaciones.exceptions;

import java.util.List;

public class GrillaIncorrectaException extends Exception {
    
    private List<String> errores;
    
    public GrillaIncorrectaException(String error) {
        super(error);
    }
    
    public GrillaIncorrectaException(List<String> errores) {
        this.errores = errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
