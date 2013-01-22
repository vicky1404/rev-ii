package cl.mdr.ifrs.exceptions;

public class ReporteSinDatosException extends Exception{
	private static final long serialVersionUID = 4284595823295238687L;

	public ReporteSinDatosException() {
        super("Informe no contiene información");
    }
    
    public ReporteSinDatosException(String error) {
        super(error);        
    }
}
