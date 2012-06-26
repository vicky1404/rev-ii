package cl.mdr.ifrs.exceptions;

import java.util.ArrayList;
import java.util.List;

public class EstadoFinancieroException extends Exception{
    
    private List<String> detailErrors;
    
    public EstadoFinancieroException() {
        super();
    }

    public EstadoFinancieroException(String mensaje) {
        super(mensaje);
    }
    
    public EstadoFinancieroException(String mensaje, List<String> detailErrors) {
        super(mensaje);
        this.detailErrors = detailErrors;
    }
    
    public EstadoFinancieroException(List<String> detailErrors) {
        super();
        this.detailErrors = detailErrors;
    }

    public void setDetailErrors(List<String> detailErrors) {
        this.detailErrors = detailErrors;
    }

    public List<String> getDetailErrors() {
        if(detailErrors==null){
            detailErrors = new ArrayList<String>();
        }
        return detailErrors;
    }
}
