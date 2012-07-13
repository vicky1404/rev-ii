package cl.mdr.ifrs.exceptions;

public class FormulaException extends Exception {
    
    private static final long serialVersionUID = 186653637317671225L;
    
    public static final int STACK_OVERFLOW = 1;
    
    private int errorCode;
    private String formula;

    public FormulaException(Throwable throwable) {
        super(throwable);
    }

    public FormulaException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public FormulaException(String string) {
        super(string);
    }
    
    public FormulaException(String string, int errorCode, String formula) {
        super(string);
        this.errorCode = errorCode;
        this.formula = formula;
    }

    public FormulaException() {
        super();
    }

    public void setErrorCode(int errorCode, String msje) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }
}
