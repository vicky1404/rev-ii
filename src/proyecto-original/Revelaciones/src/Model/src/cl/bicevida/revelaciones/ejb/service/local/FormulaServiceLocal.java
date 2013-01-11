package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.exceptions.FormulaException;

import javax.ejb.Local;

@Local
public interface FormulaServiceLocal {
    
    void processStaticFormula(final Grilla grid) throws FormulaException, Exception;
    
    void processStaticFormulaBySubGrilla(final SubGrilla subGrid) throws FormulaException, Exception;
    
    void processDynamicFomula(final Grilla grid) throws FormulaException, Exception;
    
    void processDynamicFomulaSubGrilla(final SubGrilla subGrid) throws FormulaException, Exception;
    
    public boolean processValidatorEEFF(Grilla grid) throws Exception;
    
    public void processValidatorEEFF(Celda cell) throws Exception;
    
}
