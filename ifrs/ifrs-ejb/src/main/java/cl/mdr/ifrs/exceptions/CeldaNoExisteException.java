package cl.mdr.ifrs.exceptions;

public class CeldaNoExisteException extends Exception {
	private static final long serialVersionUID = -7510259357727773431L;

	public CeldaNoExisteException(String mensaje) {
        super(mensaje);
    }
}
