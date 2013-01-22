package cl.mdr.ifrs.exceptions;

public class RegistroNoEditableException extends Exception {
	private static final long serialVersionUID = -2145283979200025472L;

	public RegistroNoEditableException() {
		super();		
	}

	public RegistroNoEditableException(String message, Throwable cause) {
		super(message, cause);		
	}

	public RegistroNoEditableException(String message) {
		super(message);		
	}
		
	public RegistroNoEditableException(Throwable cause) {
		super(cause);		
	}
	
	

}
