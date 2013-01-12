package cl.mdr.ifrs.ejb.service.local;


import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.exceptions.FormulaException;

@Local
public interface FormulaServiceLocal {
    
    void processStaticFormula(final Grilla grid) throws FormulaException, Exception;
    
    void processDynamicFomula(final Grilla grid) throws Exception;
    
    boolean processValidatorEEFF(Grilla grid) throws Exception;
    
}
