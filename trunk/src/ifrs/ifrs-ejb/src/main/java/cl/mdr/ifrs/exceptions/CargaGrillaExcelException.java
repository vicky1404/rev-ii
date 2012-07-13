package cl.mdr.ifrs.exceptions;

import java.util.List;

public class CargaGrillaExcelException extends Exception {
    private static final long serialVersionUID = 4217452240477601475L;
    
    private List<String> detailErrors;

    public CargaGrillaExcelException(String mensaje) {
        super(mensaje);
    }
    
    public CargaGrillaExcelException(String mensaje, List<String> detailErrors) {
        super(mensaje);
        this.detailErrors = detailErrors;
    }

    public void setDetailErrors(List<String> detailErrors) {
        this.detailErrors = detailErrors;
    }

    public List<String> getDetailErrors() {
        return detailErrors;
    }
}
