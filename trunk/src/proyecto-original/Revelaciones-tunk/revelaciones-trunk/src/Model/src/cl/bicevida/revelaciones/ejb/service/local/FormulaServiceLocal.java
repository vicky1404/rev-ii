package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.exceptions.FormulaException;

import java.io.Serializable;

import javax.ejb.Remote;


@Remote
public interface FormulaServiceLocal extends Serializable {
    
    void processStaticFormula(final Grilla grid) throws FormulaException, Exception;
    
    void processStaticFormulaBySubGrilla(final SubGrilla subGrid) throws FormulaException, Exception;
    
    void processDynamicFomula(final Grilla grid) throws FormulaException, Exception;
    
    void processDynamicFomulaSubGrilla(final SubGrilla subGrid) throws FormulaException, Exception;
    
    boolean processValidatorEEFF(Grilla grid) throws Exception;
    
    void processValidatorEEFF(Celda cell) throws Exception;
    
    boolean processValidatorGrillas(Grilla grid) throws Exception;
    
}
