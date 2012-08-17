package cl.bicevida.revelaciones.ejb.service;


import cl.bicevida.revelaciones.ejb.common.TipoDatoEnum;
import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.service.local.FormulaServiceLocal;
import cl.bicevida.revelaciones.exceptions.FormulaException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;


/**
 * EJB encargado de hacer operaciones matematicas sobre las celdas,
 * las operaciones pueden ser sobre formulas dinámicas y estáticas.
 * @author rodrigo.diazv@bicevida.cl
 * @since 03/05/2011
 */
@Stateless
public class FormulaServiceBean implements FormulaServiceLocal{
    
    private final String hPrefix = "h-";
    private final String vPrefix = "v-";
    private final char subtract = '-';
    private final char add = '+';
    
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
                    key = hPrefix+cell.getParentHorizontal();
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
                        key = vPrefix+cell.getParentVertical();
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
            
            if(cellOperator != add && cellOperator != subtract){
                cellOperator = add;
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
    
    private Map<String,List<Celda>> convertCellToMapList(List<Columna> columns){
        
        Map<String,List<Celda>> cellMap = new LinkedHashMap<String,List<Celda>>();
        String key;
        for(Columna column : columns){
            for(Celda cell : column.getCeldaList()){
                if(cell.getChildHorizontal()!=null){
                    key = hPrefix+cell.getChildHorizontal();
                    if(cellMap.containsKey(key)){
                        cellMap.get(key).add(cell);
                    }else{
                        List<Celda> cells = new ArrayList<Celda>();
                        cells.add(cell);
                        cellMap.put(key, cells);
                    }
                }
                if(cell.getChildVertical()!=null){
                    key = vPrefix+cell.getChildVertical();
                    if(cellMap.containsKey(key)){
                        cellMap.get(key).add(cell);
                    }else{
                        List<Celda> cells = new ArrayList<Celda>();
                        cells.add(cell);
                        cellMap.put(key, cells);
                    }
                }
            }
        }
        
        return cellMap;
    }
    
    private BigDecimal calculateMathematicalHorizontalFormula(final Celda cell,
                                                              final Map<String,List<Celda>> cellMap, 
                                                              final Map<String, BigDecimal> resultMap) throws FormulaException{
        
        String key;
        BigDecimal suma = new BigDecimal("0");
        key = hPrefix+cell.getParentHorizontal();
        List<Celda> cells = cellMap.get(key);
        if(cells!=null){
            for(Celda cellTemp : cells){
                
                if(cellTemp.getParentHorizontal()!=null){
                
                    String keyTemp = hPrefix+cellTemp.getParentHorizontal();
                    
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
                        suma = applyOperator(add, suma, new BigDecimal(cellTemp.getValor()));
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
        key = vPrefix+cell.getParentVertical();
        List<Celda> cells = cellMap.get(key);
        if(cells!=null){
            for(Celda cellTemp : cells ){
                if(cellTemp.getParentVertical()!=null){
                    
                    String keyTemp = vPrefix+cellTemp.getParentVertical();
                    
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
                        suma = applyOperator(add, suma, new BigDecimal(cellTemp.getValor()));
                }
            }
        }
        resultMap.put(key, suma);
        return suma;
    }
        
    private BigDecimal applyOperator(char operator, BigDecimal value, BigDecimal result){
        
        if(operator == subtract)
            value = value.subtract(result);
        else
            value = value.add(result);
        
        return value;
    }
    
    private boolean validateTokens(String cellKey,String invalidFormula){
        StringTokenizer cellKeys = new StringTokenizer(invalidFormula, "+");
        while(cellKeys.hasMoreTokens()){
            if(cellKeys.nextToken().equals(cellKey))
                return false;
        }
        return true;
    }
    
    private Map<String, Celda> convertCellToMap(final List<Columna> columns){
        
        Map<String, Celda> cellMap = new HashMap<String, Celda>();
        
        for(Columna column : columns){
            for(Celda cell : column.getCeldaList()){
                cellMap.put(Util.formatCellKey(cell), cell);
            }
        }
        
        return cellMap;
    }
                
    
    
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
                
                for(RelacionEeff relEeff : cell.getRelacionEeffList()){
                    sum = sum.add(Util.getBigDecimal(relEeff.getMontoTotal(), new BigDecimal(0)));
                }
                
                for(RelacionDetalleEeff relDetEeff : cell.getRelacionDetalleEeffList()){
                    sum = sum.add(Util.getBigDecimal(relDetEeff.getMontoPesos(), new BigDecimal(0)));
                }
                
                if(Util.esListaValida(cell.getRelacionEeffList()) || Util.esListaValida(cell.getRelacionDetalleEeffList())){
                    if(!cell.getValorBigDecimal().equals(sum)){
                        cell.setValid(false);
                        isValid = false;
                    }
                }
                
                cell.setSumaEeff(sum);
            }
        }
        
        return isValid;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void processValidatorEEFF(Celda cell) throws Exception{
        
        BigDecimal sum = new BigDecimal(0);
        
        for(RelacionEeff relEeff : cell.getRelacionEeffList()){
            sum = sum.add(Util.getBigDecimal(relEeff.getMontoTotal(), new BigDecimal(0)));
        }
        
        for(RelacionDetalleEeff relDetEeff : cell.getRelacionDetalleEeffList()){
            sum = sum.add(Util.getBigDecimal(relDetEeff.getMontoPesos(), new BigDecimal(0)));
        }
        
            if(Util.esListaValida(cell.getRelacionEeffList()) || Util.esListaValida(cell.getRelacionDetalleEeffList())){
                if(!cell.getValorBigDecimal().equals(sum)){
                    cell.setValid(false);
                }
            }
        
        cell.setSumaEeff(sum);
    }
}
