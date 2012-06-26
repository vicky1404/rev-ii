package cl.mdr.ifrs.ejb.test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cl.mdr.ifrs.ejb.entity.Celda;


public class FormulaTest {
    public FormulaTest() {
        super();
    }
    
    public static void main(String[] args){
        FormulaTest formula = new FormulaTest();
        formula.procesar();
        
    }
    
    public void procesar2(){
        StringTokenizer cellKeys = new StringTokenizer("+[1];+[2];+[3];+[4];-[5];-[6];-[7];+[8];-[9];",";");
        while(cellKeys.hasMoreTokens()){
            String cellKey = cellKeys.nextToken();
            //System.out.println(cellKeys.nextElement());
            System.out.println(cellKey);
        }
        
    }
    
    public void procesar(){

        Map<String, Celda> cellMap = convertCellToMap(celdasDummy());
        Map<String, BigDecimal>  resultMap = new HashMap<String, BigDecimal>();
        BigDecimal suma = new BigDecimal("0");
        
        for(Celda celda : celdasDummy()){
            if(celda.getFormula()!=null){
                String cellKey = formatCellKey(celda);
                if(!resultMap.containsKey(cellKey)){
                    try {
                        suma = calculateMathematicalFormula(celda.getFormula(), cellMap, resultMap,cellKey);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else{
                    suma = resultMap.get(cellKey);
                }
                System.out.println("Resultado Celda"+ celda.getIdFila() +"->"+suma);
            }
        }
        
    }
    
    private BigDecimal calculateMathematicalFormula(final String formula, 
                                                    final Map<String, Celda> cellMap, 
                                                    final Map<String, BigDecimal> resultMap,
                                                    String invalidFormula) throws Exception{
        
        StringTokenizer cellKeys = new StringTokenizer(formula, ";");
        BigDecimal suma = new BigDecimal("0");
        while(cellKeys.hasMoreTokens()){
            
            String cellKey = cellKeys.nextToken();
            char cellOperator = cellKey.charAt(0);
            
            if(cellOperator != '+' && cellOperator != '-'){
                cellOperator = '+';
            }else{
                cellKey = cellKey.substring(1);
            }
            
            if(cellMap.containsKey(cellKey)){
                Celda cell = cellMap.get(cellKey);
                System.out.println(cellOperator=='+'?"Sumando":"Restando" + " Token->"+cellKey);
                if(cell.getFormula()!=null && !cell.getFormula().trim().equals("")){
                    
                    if(!validateTokens(cellKey,invalidFormula)){
                        throw new RuntimeException("Error dependencia ciclica en formula (StackOverFlow)");
                    }
                    
                    invalidFormula += ("+"+cellKey);
                    BigDecimal result = calculateMathematicalFormula(cell.getFormula(), cellMap, resultMap,invalidFormula);
                    
                    suma = applyOperator(cellOperator, suma, result);
                    resultMap.put(cellKey, result);                  
                    
                }else{
                    suma = applyOperator(cellOperator, suma, new BigDecimal(cell.getValor()));
                }
            }
        }
        return suma;
    }
    
    private BigDecimal applyOperator(char operator, BigDecimal value, BigDecimal result){
        
        if(operator == '-')
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
    
    private Map<String, Celda> convertCellToMap(final List<Celda> celdas){
        
        Map<String, Celda> cellMap = new HashMap<String, Celda>();
        
        for(Celda cell : celdas){
            cellMap.put(formatCellKey(cell), cell);
        }
        
        return cellMap;
    }
    
    private String formatCellKey(final Celda cell){
        return  "["
                .concat(cell.getIdColumna().toString())
                .concat(",")
                .concat(cell.getIdFila().toString())
                .concat("]");
    }
    
    private List<Celda> celdasDummy(){
        List<Celda> celdas = new ArrayList<Celda>();
        
        Celda celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(1L);
        celda.setValor("1000");
        celdas.add(celda);
        
        celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(2L);
        celda.setValor("2000");
        celdas.add(celda);
        
        celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(3L);
        celda.setValor("");
        celda.setFormula("-[1,1];-[1,5]");
        celdas.add(celda);
        
        celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(4L);
        celda.setValor("5000");
        celdas.add(celda);
        
        celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(5L);
        celda.setValor("");
        celda.setFormula("-[1,4];+[1,2];+[1,6]");
        celdas.add(celda);
        
        celda = new Celda();
        celda.setIdColumna(1L);
        celda.setIdFila(6L);
        celda.setValor("550");
        celda.setFormula("-[1,4];+[1,2]");
        celdas.add(celda);
        
        return celdas;
    }
}
