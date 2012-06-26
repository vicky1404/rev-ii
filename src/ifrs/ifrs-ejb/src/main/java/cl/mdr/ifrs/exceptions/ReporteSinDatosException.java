package cl.mdr.ifrs.exceptions;

public class ReporteSinDatosException extends Exception{
    public ReporteSinDatosException() {
        super("Informe no contiene informaci�n");
    }
    
    public ReporteSinDatosException(String error) {
        super(error);        
    }
}
