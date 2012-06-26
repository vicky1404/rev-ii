package cl.bicevida.revelaciones.ejb.exception;

public class ReporteSinDatosException extends Exception{
    public ReporteSinDatosException() {
        super("Informe no contiene información");
    }
    
    public ReporteSinDatosException(String error) {
        super(error);        
    }
}
