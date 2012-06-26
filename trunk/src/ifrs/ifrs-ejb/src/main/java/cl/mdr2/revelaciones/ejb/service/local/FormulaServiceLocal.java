package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.exceptions.FormulaException;

import javax.ejb.Local;

@Local
public interface FormulaServiceLocal {
    
    void processStaticFormula(final Grilla grid) throws FormulaException, Exception;
    
    void processDynamicFomula(final Grilla grid) throws Exception;
    
}
