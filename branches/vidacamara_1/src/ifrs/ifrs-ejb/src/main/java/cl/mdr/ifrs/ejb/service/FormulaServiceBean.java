package cl.mdr.ifrs.ejb.service;


import static cl.mdr.ifrs.ejb.cross.FormulaHelper.H_PREFIX;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.SIGNO_RESTA;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.SIGNO_SUMA;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.V_PREFIX;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.convertCellToMap;
import static cl.mdr.ifrs.ejb.cross.FormulaHelper.convertCellToMapList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import cl.mdr.ifrs.ejb.common.TipoDatoEnum;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.ejb.service.local.FormulaServiceLocal;
import cl.mdr.ifrs.exceptions.FormulaException;


/**
 * EJB encargado de hacer operaciones matematicas sobre las celdas,
 * las operaciones pueden ser sobre formulas din�micas y est�ticas. 
 * @since 03/05/2011
 */
@Stateless
public class FormulaServiceBean implements FormulaServiceLocal{

    @EJB
    FacadeServiceLocal facadeService;
    
    public FormulaServiceBean() {
        super();
    }

    /**
     * @param grid
     * @throws Exception
     * Método que suma las celdas de una grilla que este configurada
     * con formulas dinámicas
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void processDynamicFomula(final Grilla grid) throws FormulaException, Exception{
        
        BigDecimal sum;
        String key;
        Map<String,BigDecimal> resultMap = new LinkedHashMap<String, BigDecimal>();
        Map<String,List<Celda>> cellMapList =  convertCellToMapList(grid.getColumnaList());
        
        for(Columna column : grid.getColumnaList()){
            
            for(Celda cell : column.getCeldaList()){
                
                if(!Util.isTotalorSubTotalNumeric(cell))
                    continue;
                
                if(cell.getParentHorizontal()!=null){
                    key = H_PREFIX+cell.getParentHorizontal();
                    if(resultMap.containsKey(key)){
                        setNumericValueToCell(resultMap.get(key),cell);
                    }else{
                        try{
                            sum = calculateMathematicalHorizontalFormula(cell, cellMapList, resultMap);
                        }catch(StackOverflowError e){
                            throw new FormulaException("Error dependencia cíclica en fórmula (StackOverFlow), revise configuración de fórmulas", 
                                                        FormulaException.STACK_OVERFLOW, Util.formatCellKey(cell));
                        }
                        setNumericValueToCell(sum,cell);
                    }
                }
                
                if(cell.getParentVertical()!=null){
                        key = V_PREFIX+cell.getParentVertical();
                        if(resultMap.containsKey(key)){
                            setNumericValueToCell(resultMap.get(key),cell);
                        }else{
                            try{
                                sum = calculateMathematicalVerticalFormula(cell, cellMapList, resultMap);
                            }catch(StackOverflowError e){
                                throw new FormulaException("Error dependencia cíclica en fórmula (StackOverFlow), revise configuración de fórmulas", 
                                                            FormulaException.STACK_OVERFLOW, Util.formatCellKey(cell));
                            }
                            setNumericValueToCell(sum,cell);
                        }
                }
            }
        }
    }

    /**
     * @param grid
     * @throws StackOverflowError
     * @throws Exception
     * Método que suma las celdas de una grilla que este configurada
     * con formulas estáticas
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void processStaticFormula(final Grilla grid) throws FormulaException, Exception {

        Map<String, Celda> cellMap = convertCellToMap(grid.getColumnaList());
        Map<String, BigDecimal>  resultMap = new HashMap<String, BigDecimal>();
        BigDecimal sum = new BigDecimal("0");
        
        for(Columna column : grid.getColumnaList()){
            for(Celda cell : column.getCeldaList()){
                if(cell.getFormula()!=null && Util.isTotalorSubTotalNumeric(cell)){
                    String cellKey = Util.formatCellKey(cell);
                    if(!resultMap.containsKey(cellKey)){
                        try{
                            sum = calculateMathematicalFormula(cell.getFormula(), cellMap, resultMap, cellKey);
                        }catch(StackOverflowError e){
                            throw new FormulaException("Error dependencia cíclica en fórmula (StackOverFlow), revise configuración de fórmulas",
                                                        FormulaException.STACK_OVERFLOW, Util.formatCellKey(cell) + " = " + cell.getFormula());
                        }
                    }else{
                        sum = resultMap.get(cellKey);
                    }
                    setNumericValueToCell(sum,cell);
                    //System.out.println("Resultado Celda"+ cell.getIdFila() +"->"+sum);
                }
            }
        }        
    }

    private BigDecimal calculateMathematicalFormula(final String formula, 
                                                    final Map<String, Celda> cellMap, 
                                                    final Map<String, BigDecimal> resultMap,
                                                    String invalidFormula) throws FormulaException{
        
        StringTokenizer cellKeys = new StringTokenizer(formula, ";");
        BigDecimal suma = new BigDecimal("0");
        while(cellKeys.hasMoreTokens()){
            
            String cellKey = cellKeys.nextToken();
            char cellOperator = cellKey.charAt(0);
            
            if(cellOperator != SIGNO_SUMA && cellOperator != SIGNO_RESTA){
                cellOperator = SIGNO_SUMA;
            }else{
                cellKey = cellKey.substring(1);
            }
            
            if(cellMap.containsKey(cellKey)){
                Celda cell = cellMap.get(cellKey);
                //System.out.println(cellOperator=='+'?"Sumando":"Restando" + " Token->"+cellKey);
                if(cell.getFormula()!=null && !cell.getFormula().trim().equals("")){
                    
                    /*if(!validateTokens(cellKey,invalidFormula)){
                        throw new FormulaException("Error dependencia cíclica en formula (StackOverFlow)", 
                                                    FormulaException.STACK_OVERFLOW, Util.formatCellKey(cell) + " = " + cell.getFormula());
                    }*/
                    
                    invalidFormula += ("+"+cellKey);
                    BigDecimal result = calculateMathematicalFormula(cell.getFormula(), cellMap, resultMap,invalidFormula);
                    
                    suma = applyOperator(cellOperator, suma, result);
                    resultMap.put(cellKey, result);                  
                    
                }else{
                    if(isNumeric(cell))
                        suma = applyOperator(cellOperator, suma, new BigDecimal(cell.getValor()));
                }
            }
        }
        return suma;
    }
    
    private BigDecimal calculateMathematicalHorizontalFormula(final Celda cell,
                                                              final Map<String,List<Celda>> cellMap, 
                                                              final Map<String, BigDecimal> resultMap) throws FormulaException{
        
        String key;
        BigDecimal suma = new BigDecimal("0");
        key = H_PREFIX+cell.getParentHorizontal();
        List<Celda> cells = cellMap.get(key);
        if(cells!=null){
            for(Celda cellTemp : cells){
                
                if(cellTemp.getParentHorizontal()!=null){
                
                    String keyTemp = H_PREFIX+cellTemp.getParentHorizontal();
                    
                    if(!resultMap.containsKey(keyTemp)){
                        BigDecimal sumaTemp = new BigDecimal("0");
                        sumaTemp = calculateMathematicalHorizontalFormula(cellTemp, cellMap, resultMap);                        
                        suma = suma.add(sumaTemp);
                        resultMap.put(keyTemp, sumaTemp);
                    }else{
                        suma = suma.add(resultMap.get(keyTemp));
                    }
                
                }else{
                    if(isNumeric(cellTemp))
                        suma = applyOperator(SIGNO_SUMA, suma, new BigDecimal(cellTemp.getValor()));
                }
                
            }
        }
        resultMap.put(key, suma);
        return suma;
    }
    
    private BigDecimal calculateMathematicalVerticalFormula(final Celda cell,
                                                              final Map<String,List<Celda>> cellMap, 
                                                              final Map<String, BigDecimal> resultMap) throws FormulaException{
        
        String key;
        BigDecimal suma = new BigDecimal("0");
        key = V_PREFIX+cell.getParentVertical();
        List<Celda> cells = cellMap.get(key);
        if(cells!=null){
            for(Celda cellTemp : cells ){
                if(cellTemp.getParentVertical()!=null){
                    
                    String keyTemp = V_PREFIX+cellTemp.getParentVertical();
                    
                    if(!resultMap.containsKey(keyTemp)){
                        BigDecimal sumaTemp = new BigDecimal("0");
                        sumaTemp = calculateMathematicalVerticalFormula(cellTemp, cellMap, resultMap);                        
                        suma = suma.add(sumaTemp);
                        resultMap.put(keyTemp, sumaTemp);
                    }else{
                        suma = suma.add(resultMap.get(keyTemp));
                    }
                    
                }else{
                    if(isNumeric(cellTemp))
                        suma = applyOperator(SIGNO_SUMA, suma, new BigDecimal(cellTemp.getValor()));
                }
            }
        }
        resultMap.put(key, suma);
        return suma;
    }
        
    private BigDecimal applyOperator(char operator, BigDecimal value, BigDecimal result){
        
        if(operator == SIGNO_RESTA)
            value = value.subtract(result);
        else
            value = value.add(result);
        
        return value;
    }
    
    /*private boolean validateTokens(String cellKey,String invalidFormula){
        StringTokenizer cellKeys = new StringTokenizer(invalidFormula, "+");
        while(cellKeys.hasMoreTokens()){
            if(cellKeys.nextToken().equals(cellKey))
                return false;
        }
        return true;
    }*/
    
    
    private void setNumericValueToCell(BigDecimal sum, Celda cell){
        if (cell.getTipoDato() != null && 
            cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
                cell.setValor(String.valueOf(sum.longValue()));
        } else if (cell.getTipoDato() != null && 
                   cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
            cell.setValor(String.valueOf(sum.doubleValue()));
        }
    }
    
    private boolean isNumeric(Celda cell){
        if ( cell.getValor()!=null &&
            (cell.getTipoDato() != null &&
             (cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())  ||
              cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())))){
            try{
                new BigDecimal(cell.getValor());
                return true;
            }catch(NumberFormatException e){
                return false;
            }
        }
        return false;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean processValidatorEEFF(Grilla grid) throws Exception{
        
        boolean isValid = true;
        
        for(Columna column : grid.getColumnaList()){
            
            for(Celda cell : column.getCeldaList()){
                
                BigDecimal sum = new BigDecimal(0);
                
                //List<RelacionEeff> relacionEeffList = facadeService.getEstadoFinancieroService().getRelacionEeffByCelda(cell);
                
                if(Util.esListaValida(cell.getRelacionEeffList())){
                
	                for(RelacionEeff relEeff : cell.getRelacionEeffList()){
	                    sum = sum.add(Util.getBigDecimal(relEeff.getMontoTotal(), new BigDecimal(0)));
	                }
                
            	}
                
                //List<RelacionDetalleEeff> relacionDetalleEeffList = facadeService.getEstadoFinancieroService().getRelacionDetalleEeffByCelda(cell);
                
                if(Util.esListaValida(cell.getRelacionDetalleEeffList())){
                
	                for(RelacionDetalleEeff relDetEeff : cell.getRelacionDetalleEeffList()){
	                    sum = sum.add(Util.getBigDecimal(relDetEeff.getMontoPesosMil(), new BigDecimal(0)));
	                }
                
                }
                
                if(Util.esListaValida(cell.getRelacionEeffList()) || Util.esListaValida(cell.getRelacionDetalleEeffList())){
  
	                if(cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.ENTERO.getKey())){
	                	if(!cell.getValorLong().equals(sum.longValue())){
	                        cell.setValid(false);
	                        isValid = false;
	                    }
	                }else if(cell.getTipoDato().getIdTipoDato().equals(TipoDatoEnum.DECIMAL.getKey())){
	                	if(!cell.getValorBigDecimal().equals(sum)){
	                        cell.setValid(false);
	                        isValid = false;
	                    }
	                }
                    
                	
                    
                }
                
                cell.setSumaEeff(sum);
            }
        }
        
        return isValid;
    }
}