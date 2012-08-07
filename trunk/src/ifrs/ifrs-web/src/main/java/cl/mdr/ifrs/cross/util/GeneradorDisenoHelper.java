package cl.mdr.ifrs.cross.util;

import cl.mdr.ifrs.ejb.common.TipoCeldaEnum;
import cl.mdr.ifrs.ejb.cross.SortHelper;
import cl.mdr.ifrs.ejb.cross.Util;
import cl.mdr.ifrs.ejb.entity.AgrupacionColumna;
import cl.mdr.ifrs.ejb.entity.Celda;
import cl.mdr.ifrs.ejb.entity.Columna;
import cl.mdr.ifrs.ejb.entity.Estructura;
import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Html;
import cl.mdr.ifrs.ejb.entity.Texto;
import cl.mdr.ifrs.ejb.entity.TipoEstructura;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.exceptions.GrillaIncorrectaException;
import cl.mdr.ifrs.model.EstructuraModel;
import cl.mdr.ifrs.vo.AgrupacionColumnaModelVO;
import cl.mdr.ifrs.vo.AgrupacionModelVO;
import cl.mdr.ifrs.cross.vo.AgrupacionVO;
import cl.mdr.ifrs.vo.GrillaModelVO;
import cl.mdr.ifrs.vo.GrillaVO;
import cl.mdr.ifrs.vo.TableModelVO;



import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;


import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.primefaces.component.column.Column;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.UploadedFile;


public class GeneradorDisenoHelper {
    
    public static final String PREFIX_ID_COLUMN_CHILD = "cNDC";
    public static final String PREFIX_ID_COLUMN_PARENT = "cNDP";
    public static final String EXCEL_EXTENSION = "xlsx";
    public static final int COPY_AGRUPACION = 1;
    public static final int CLONE_AGRUPACION = 2;
    
    public GeneradorDisenoHelper() {
    }
    
    public static Long getGrupoNivel1(Map<Long, AgrupacionColumnaModelVO> nivel1Map, Long idColumna){
        
        Iterator it = nivel1Map.entrySet().iterator();
        
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            
            AgrupacionColumnaModelVO agrupacionN1VO = (AgrupacionColumnaModelVO)entry.getValue();
            for(Columna columna : agrupacionN1VO.getColumnas()){
                if(columna.getIdColumna() == idColumna){
                    return agrupacionN1VO.getAgrupacion().getGrupo();
                }
            }
        }
        
        return null;
        
    }
    
    
    public static void procesarVersion(List<Version> versiones, List<Estructura> estructuras, Map<Long, GrillaModelVO> grillaModelMap) throws Exception{
        
        int ultimaVersion = versiones.size() -1;
        
        Version versionVigente = versiones.get(ultimaVersion);
        
        for(Estructura estructura : estructuras){
            estructura.setVersion(versionVigente);
            if(grillaModelMap.containsKey(estructura.getOrden())){
                GrillaModelVO grillaModel = grillaModelMap.get(estructura.getOrden());
                if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){
                    Grilla grilla = new Grilla();
                    grilla.setEstructura(estructura);
                    grilla.setColumnaList(grillaModel.getColumnas());
                    estructura.setGrilla(grilla);
                }else if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                    Texto texto = new Texto();
                    texto.setEstructura(estructura);
                    estructura.setTexto(texto);
                }else{
                    Html html = new Html();
                    html.setEstructura(estructura);
                    estructura.setHtml(html);
                }
            }
        }
        versionVigente.setEstructuraList(estructuras);
        
    }
    
    @SuppressWarnings("rawtypes")
	public static void validarContenidoCelda(Map<Long, EstructuraModel> estructuraModelMap) throws GrillaIncorrectaException, Exception{
        Iterator it = estructuraModelMap.entrySet().iterator();
        List<String> mensajes = new ArrayList<String>();
        String mensaje;
        boolean valido = true;
        int i=0;
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            EstructuraModel estructuraModel = (EstructuraModel)entry.getValue();
            if(TipoEstructura.ESTRUCTURA_TIPO_GRILLA.equals(estructuraModel.getTipoEstructura())){
                if(estructuraModel.getColumnas()==null || estructuraModel.getColumnas().size()==0)
                    throw new GrillaIncorrectaException("La grilla configurada debe tener al menos una columna");
                for(Columna columna : estructuraModel.getColumnas()){
                    if(columna.getCeldaList()==null || columna.getCeldaList().size()==0)
                        throw new GrillaIncorrectaException("La grilla debe tener al menos una celda por columna");
                    for(Celda celda : columna.getCeldaList()){
                        Long fila = celda.getIdFila();
                        if(celda.getTipoCelda()==null || celda.getTipoDato() == null || 
                           celda.getTipoCelda().getIdTipoCelda()==null || celda.getTipoDato().getIdTipoDato()==null){
                            if(valido){
                                mensaje = "Errores en Estructura N° " + entry.getKey();
                                mensajes.add(mensaje);
                            }
                            mensaje = "Columna :" + columna.getTituloColumna() + " - Fila : " + celda.getIdFila() + " -> Debe ingresar el tipo de dato y celda ";
                            mensajes.add(mensaje);
                            valido = false;
                        }
                    }   
                }
                valido = true;
            }
        }
        
        if(mensajes.size()>0)
            throw new GrillaIncorrectaException(mensajes);
    }
    
    public static TableModelVO createTableModel(List<Columna> columnasGrilla) {
        
        TableModelVO tableModel;
        SortHelper.sortColumnasByOrden(columnasGrilla);
        boolean listaVacia = true;
        List<Columna> columnas = new ArrayList<Columna>();
        List<Map<Long,Celda>> rows = new ArrayList<Map<Long,Celda>>();
        Map<Long,Celda> celdaMap = new LinkedHashMap<Long,Celda>();

        for(Columna columnaNota : columnasGrilla){
            columnas.add(columnaNota);
            if(columnaNota.getCeldaList()==null)
                continue;
            
            int j=0;
            for(Celda celda : columnaNota.getCeldaList()){
                celda.setIdColumna(Integer.valueOf(j).longValue()+1L);
                if(listaVacia){
                    celdaMap = new LinkedHashMap<Long,Celda>();
                    celdaMap.put(new Long(columnaNota.getIdColumna()), celda);
                    rows.add(celdaMap);
                }else{
                    if(rows.size()>=j){
                        rows.get(j).put(new Long(columnaNota.getIdColumna()), celda);
                    }
                }
                j++;                    
            }
            listaVacia = false;
        }
        
        tableModel = new TableModelVO(columnas, rows);
            
        return tableModel;
    }
    
    public static void procesarColumnas(List<Columna> columnas){
        
        int numCelda=0;
        
        if(columnas==null)
            return;
        
        for(Columna columna : columnas){
            if(columna.getCeldaList()!=null)
                numCelda = columna.getCeldaList().size() > numCelda ? columna.getCeldaList().size() : numCelda;
        }
        
        for(Columna columna : columnas){
            if(columna.getCeldaList()==null){
                columna.setCeldaList(new ArrayList<Celda>());
            }
            if(columna.getCeldaList().size() < numCelda){
                int celdas = numCelda - columna.getCeldaList().size();
                Long idCelda = 0L;
                for(int i=0; i<celdas; i++){
                    idCelda = columna.getCeldaList().size() +1L;
                    columna.getCeldaList().add(new Celda(columna.getIdColumna(),idCelda));
                }
            }
        }
        
    }
    /**
     * Crea una lista de agrupaciones para representarla en html ya que html se contruye por fila
     * la lista de agrupaciones debe venir ordenada
     * @param agrupaciones
     * @return
     */
    public static List<List<AgrupacionModelVO>> crearAgrupadorHTMLVO(List<AgrupacionColumna> agrupaciones){
    	
    	List<List<AgrupacionModelVO>> agrupacionesVO = new ArrayList<List<AgrupacionModelVO>>();
    	Map<Long,List<AgrupacionModelVO>> agrupacionMap = new LinkedHashMap<Long,List<AgrupacionModelVO>>();
        
        if(!Util.esListaValida(agrupaciones))
            return agrupacionesVO;
        
        Long min=100L;
        Long max=0L;
        AgrupacionColumna agrupacionPaso =null;
        int contador = 0;
        
        for(AgrupacionColumna agrupacion : agrupaciones){
        	
            contador++;
            
            if(agrupacionPaso !=null && agrupacion.getGrupo()==agrupacionPaso.getGrupo()){
                if(min>agrupacion.getIdColumna()){
                    min=agrupacion.getIdColumna();
                }
                if(max<agrupacion.getIdColumna()){
                    max=agrupacion.getIdColumna();
                }
            }else{
                if(agrupacionPaso!=null){
                	setAgrupadorMap(agrupacionMap, new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacionPaso.getAncho(), agrupacionPaso.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
                }
                agrupacionPaso = agrupacion;
                min = agrupacion.getIdColumna();
                max = agrupacion.getIdColumna();
            }
            if(contador == agrupaciones.size()){
            	setAgrupadorMap(agrupacionMap,new AgrupacionModelVO(min, max, agrupacionPaso.getGrupo(), agrupacion.getAncho(), agrupacion.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
            }
            
        }
        
        for(List<AgrupacionModelVO> agrupacionesNew : agrupacionMap.values()){
        	agrupacionesVO.add(agrupacionesNew);
        }
        
        return agrupacionesVO;
    	
    }
    
    private static void setAgrupadorMap(Map<Long,List<AgrupacionModelVO>> agrupacionMap, AgrupacionModelVO agrupacion){
    	
    	if(agrupacionMap.containsKey(agrupacion.getNivel())){
    		agrupacionMap.get(agrupacion.getNivel()).add(agrupacion);
    	}else{
    		List<AgrupacionModelVO> agrupaciones = new ArrayList<AgrupacionModelVO>();
    		agrupaciones.add(agrupacion);
    		agrupacionMap.put(agrupacion.getNivel(), agrupaciones);
    	}
    }
    
    
    /**
     * Crea una lista de agrupaciones para representarla en dataTable, ya que jsf construye por columna
     * @param agrupaciones
     * @return
     */
    public static List<AgrupacionVO> crearAgrupadorVO(List<AgrupacionColumna> agrupaciones){
        
        List<AgrupacionVO> agrupacionesVO = new ArrayList<AgrupacionVO>();
        
        if(!Util.esListaValida(agrupaciones))
            return agrupacionesVO;
        
        Long min=100L;
        Long max=0L;
        AgrupacionColumna agrupacionPaso =null;
        int contador = 0;
        for(AgrupacionColumna agrupacion : agrupaciones){
            
            contador++;
            
            if(agrupacionPaso !=null && agrupacion.getGrupo()==agrupacionPaso.getGrupo()){
                if(min>agrupacion.getIdColumna()){
                    min=agrupacion.getIdColumna();
                }
                if(max<agrupacion.getIdColumna()){
                    max=agrupacion.getIdColumna();
                }
            }else{
                if(agrupacionPaso!=null){
                    agrupacionesVO.add(new AgrupacionVO(min, max, agrupacionPaso.getGrupo(), agrupacionPaso.getAncho(), agrupacionPaso.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
                }
                agrupacionPaso = agrupacion;
                min = agrupacion.getIdColumna();
                max = agrupacion.getIdColumna();
            }
            if(contador == agrupaciones.size()){
                agrupacionesVO.add(new AgrupacionVO(min, max, agrupacionPaso.getGrupo(), agrupacion.getAncho(), agrupacion.getTitulo(),agrupacionPaso.getIdGrilla(),agrupacionPaso.getIdNivel()));
            }
            
            }
        
        return agrupacionesVO;
    }
    
    public static Map<Long, List<AgrupacionVO>> crearAgrupadorVO(Map<Long,List<AgrupacionColumna>> agrupacionesMap, List<Long> niveles){
        
        Map<Long, List<AgrupacionVO>> agrupadorMap = new HashMap<Long, List<AgrupacionVO>>();
        
        if(!cl.mdr.ifrs.ejb.cross.Util.esListaValida(niveles) || agrupacionesMap==null)
            return agrupadorMap;
        
        for(Long nivel : niveles){
            Long min=100L;
            Long max=0L;
            //AgrupacionVO grupo=null;
            AgrupacionColumna agrupacionPaso =null;
            int contador = 0;
            for(AgrupacionColumna agrupacion : agrupacionesMap.get(nivel)){
                
                contador++;
                
                if(agrupacionPaso !=null && agrupacion.getGrupo()==agrupacionPaso.getGrupo()){
                    if(min>agrupacion.getIdColumna()){
                        min=agrupacion.getIdColumna();
                    }
                    if(max<agrupacion.getIdColumna()){
                        max=agrupacion.getIdColumna();
                    }
                }else{
                    if(agrupacionPaso==null){
                        List<AgrupacionVO> agrupacionesVO = new ArrayList<AgrupacionVO>();
                        agrupadorMap.put(nivel, agrupacionesVO);
                    }else{
                        agrupadorMap.get(nivel).add(new AgrupacionVO(min, max, agrupacionPaso.getGrupo(), agrupacionPaso.getAncho(), agrupacionPaso.getTitulo(), agrupacionPaso.getIdGrilla()));
                    }
                    agrupacionPaso = agrupacion;
                    min = agrupacion.getIdColumna();
                    max = agrupacion.getIdColumna();
                }
                if(contador == agrupacionesMap.get(nivel).size()){
                    agrupadorMap.get(nivel).add(new AgrupacionVO(min, max, agrupacionPaso.getGrupo(), agrupacion.getAncho(), agrupacion.getTitulo(), agrupacionPaso.getIdGrilla()));
                }
            
            }
        }
        
        return agrupadorMap;
    }
    
    public static Long getContadorFilaByColumnas(List<Columna> columnas){
        Long contador = 0L;
        for(Celda celda : columnas.get(0).getCeldaList()){
                contador++;
        }
        return contador;
    }
    
    @Deprecated
    public static Map<Long, GrillaModelVO> createGrillaModel(List<Estructura> estructuras){
        
        Map<Long, GrillaModelVO> grillaModelMap = new LinkedHashMap<Long, GrillaModelVO>();
        
        Long i=1L;
        
        for(Estructura estructura : estructuras){
            
        	if(estructura.getGrilla() != null){
	            GrillaModelVO grillaModel = new GrillaModelVO();
	            grillaModel.setTituloGrilla(estructura.getGrilla().getTitulo());
	            List<Long> niveles = new ArrayList<Long>();
	            Map<Long,List<AgrupacionColumna>> agrupacionesMap = new LinkedHashMap<Long,List<AgrupacionColumna>>();
	            for(Columna columna : estructura.getGrilla().getColumnaList()){
	                createAgrupadorMap(columna, niveles, agrupacionesMap, COPY_AGRUPACION);
	            }
	            grillaModel.setColumnas(estructura.getGrilla().getColumnaList());
	            grillaModel.setIdGrilla(estructura.getGrilla().getIdGrilla());
	            grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_GRILLA);
	            grillaModel.setNivelesAgregados(niveles);
	            grillaModel.setAgrupacionesMap(agrupacionesMap);
	            Long cantidadFila = GeneradorDisenoHelper.getContadorFilaByColumnas(estructura.getGrilla().getColumnaList());
	            List<Long> filas = new ArrayList<Long>();
	            for(Long j=1L; j<=cantidadFila; j++){
	                filas.add(j);
	            }
	            grillaModel.setFilas(filas);
	            grillaModelMap.put(i, grillaModel);
	            i++;
        	}
                
            if (estructura.getTexto() != null){
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setTexto(estructura.getTexto());
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_TEXTO);
                grillaModelMap.put(i, grillaModel);
                i++;
            }
            
            if (estructura.getHtml() != null){
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setHtml(estructura.getHtml());
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_HTML);
                grillaModelMap.put(i, grillaModel);
                i++;
            } 
        }

        return grillaModelMap;

    }
    
    /**
     * Crea un map con el contenido de EstructuraModel indexado por el orden adquirido por la estructura en la 
     * formacion constituida por el user.
     * @param estructuras
     * @return LinkedHashMap
     */
    public static Map<Long, EstructuraModel> createEstructuraModel(List<Estructura> estructuras){        
        Map<Long, EstructuraModel> estructuraModelMap = new LinkedHashMap<Long, EstructuraModel>();        
        Long i=1L;        
        for(Estructura estructura : estructuras){
            
        	if(estructura.getGrilla() != null){
        		EstructuraModel estructuraModel = new EstructuraModel();
        		estructuraModel.setTituloGrilla(estructura.getGrilla().getTitulo());
	            List<Long> niveles = new ArrayList<Long>();
	            Map<Long,List<AgrupacionColumna>> agrupacionesMap = new LinkedHashMap<Long,List<AgrupacionColumna>>();
	            for(Columna columna : estructura.getGrilla().getColumnaList()){
	                createAgrupadorMap(columna, niveles, agrupacionesMap, COPY_AGRUPACION);
	            }
	            estructuraModel.setColumnas(estructura.getGrilla().getColumnaList());
	            estructuraModel.setIdGrilla(estructura.getGrilla().getIdGrilla());
	            estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_GRILLA);
	            estructuraModel.setNivelesAgregados(niveles);
	            estructuraModel.setAgrupacionesMap(agrupacionesMap);
	            Long cantidadFila = GeneradorDisenoHelper.getContadorFilaByColumnas(estructura.getGrilla().getColumnaList());
	            List<Long> filas = new ArrayList<Long>();
	            for(Long j=1L; j<=cantidadFila; j++){
	                filas.add(j);
	            }
	            estructuraModel.setFilas(filas);
	            estructuraModelMap.put(i, estructuraModel);
	            i++;
        	}
                
            if (estructura.getTexto() != null){
                EstructuraModel estructuraModel = new EstructuraModel();
                estructuraModel.setTexto(estructura.getTexto());
                estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_TEXTO);
                estructuraModelMap.put(i, estructuraModel);
                i++;
            }
            
            if (estructura.getHtml() != null){
                EstructuraModel estructuraModel = new EstructuraModel();
                estructuraModel.setHtml(estructura.getHtml());
                estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_HTML);
                estructuraModelMap.put(i, estructuraModel);
                i++;
            } 
        }
        return estructuraModelMap;
    }
    
    public static Column getRichColumn(String titulo, Long ancho, String alineacion, boolean rowHeader, String id){
        Column column = new Column();
        column.setHeaderText(titulo);
        //TODO
        /*column.setAlign(alineacion);
        column.setRowHeader(rowHeader);*/
        
        column.setRendered(true);
        column.setWidth(ancho.intValue());
        column.setId(id);
        return column;
    }
    
    public static HtmlOutputText getRichOutputText(ValueExpression value){
    	HtmlOutputText oText = new HtmlOutputText();
        oText.setValueExpression("value", value);
        oText.setRendered(true);
        return oText;
    }
    
    public static void createAgrupadorMap(Columna columna, List<Long> niveles, Map<Long,List<AgrupacionColumna>> agrupacionesMap, int tipo){
        
        if(columna.getAgrupacionColumnaList()!=null){
            for(AgrupacionColumna agrupacion : columna.getAgrupacionColumnaList()){
                agrupacion.setIdGrilla(null);
                if(agrupacionesMap.containsKey(agrupacion.getIdNivel())){
                    if(tipo == COPY_AGRUPACION)
                        agrupacionesMap.get(agrupacion.getIdNivel()).add(agrupacion);
                    else{
                        AgrupacionColumna agrupacionNew = AgrupacionColumna.clone(agrupacion);
                        agrupacionesMap.get(agrupacion.getIdNivel()).add(agrupacion);
                    }
                }else{
                    List<AgrupacionColumna> agrupacionList = new ArrayList<AgrupacionColumna>();
                    if(tipo == COPY_AGRUPACION)
                        agrupacionList.add(agrupacion);
                    else
                        agrupacionList.add(AgrupacionColumna.clone(agrupacion));
                    
                    agrupacionesMap.put(agrupacion.getIdNivel(), agrupacionList);
                    niveles.add(agrupacion.getIdNivel());
                }
            }
            
        }
    }
    
    public static Map<Long, EstructuraModel> cloneEstructuraModel(List<Estructura> estructuras){
        
        Map<Long, EstructuraModel> estructuraModelMap = new LinkedHashMap<Long, EstructuraModel>();
        
        Long i=1L;
        
        for(Estructura estructura : estructuras){
        	
            if (estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_GRILLA){            	
                Grilla grillaNew = new Grilla();                
                List<Long> niveles = new ArrayList<Long>();
                grillaNew.setTitulo(estructura.getGrilla() .getTitulo());
                grillaNew.setColumnaList(estructura.getGrilla() .getColumnaList());
                grillaNew.setEstructura(estructura.getGrilla() .getEstructura());
                
                EstructuraModel estructuraModel = new EstructuraModel();
                estructuraModel.setTituloGrilla(grillaNew.getTitulo());
                List<Columna> columnasNew = new ArrayList<Columna>();
                for(Columna columna : grillaNew.getColumnaList()){
                    
                    Columna columnaNew = new Columna();
                    columnaNew.setAncho(columna.getAncho());
                    columnaNew.setIdColumna(columna.getIdColumna());
                    columnaNew.setOrden(columna.getOrden());
                    columnaNew.setTituloColumna(columna.getTituloColumna());
                    columnaNew.setCeldaList(columna.getCeldaList());
                    columnaNew.setGrilla(columna.getGrilla());
                    columnaNew.setRowHeader(columna.isRowHeader());
                    columnaNew.setAgrupacionColumnaList(columna.getAgrupacionColumnaList());
                                                            
                    if(columnaNew.getCeldaList()!=null){
                        for(Celda celda : columna.getCeldaList()){
                            celda.setIdGrilla(null);
                        }
                    }
                    
                    columnasNew.add(columnaNew);
                }
                estructuraModel.setColumnas(columnasNew);
                estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_GRILLA);                                
                estructuraModel.setNivelesAgregados(niveles);
                estructuraModelMap.put(i, estructuraModel);
                i++;
            }  
            
            if (estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){                
            	EstructuraModel estructuraModel = new EstructuraModel();
                estructura.getTexto().setIdTexto(null);
                estructuraModel.setTexto(estructura.getTexto());
                estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_TEXTO);
                estructuraModelMap.put(i, estructuraModel);
                i++;
            }
            
            if (estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_HTML){
            	EstructuraModel estructuraModel = new EstructuraModel();
                estructura.getHtml().setIdHtml(null);
                estructuraModel.setHtml(estructura.getHtml());
                estructuraModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_HTML);
                estructuraModelMap.put(i, estructuraModel);
                i++;
            }
            
        }
        
        return estructuraModelMap;
    }
    
    
    
    public static void setListaPorNivelUsado(List<AgrupacionColumnaModelVO> agrupacionColumnaList, GrillaVO grilla){
    	if (agrupacionColumnaList !=null && agrupacionColumnaList.isEmpty() ){
    		grilla.setNivel(agrupacionColumnaList.get(0).getNivel());
        }else{
        	grilla.setNivel(0L);
        }
    }
    
  //TODO cambiar metodo de posicion
    
    
    private static boolean listaValida(List list){
        
        if(list==null || list.size()==0)
            return false;
        
        return true;
    }
    
    public static void agregarFilaGrillaByFilaSelected(Grilla grilla, Long filaSelected){
        
        Celda celdaNueva;
        
        Long parentH = getMaxHorizontalParent(grilla);
                
        for(Columna columna : grilla.getColumnaList()){
            
            Long contador =1L;
           
            
            if(columna.getCeldaList()==null){
                
                columna.setCeldaList(new ArrayList<Celda>());
                celdaNueva = new Celda(columna.getIdColumna(),1L);
                columna.getCeldaList().add(celdaNueva);
                
            }else{
                List<Celda> celdaPasoList = new ArrayList<Celda>();
                for(Celda celda : columna.getCeldaList()){
                    celda.setIdFila(contador);
                    celdaPasoList.add(celda);
                    if(celda.getIdFila().equals(filaSelected)){
                        contador++;
                        celdaNueva = new Celda(columna.getIdColumna(),contador);
                        celdaNueva.setColumna(columna);
                        celdaNueva.setTipoCelda(celda.getTipoCelda());
                        celdaNueva.setIdGrilla(celda.getIdGrilla());
                        celdaNueva.setTipoDato(celda.getTipoDato());
                        celdaNueva.setChildVertical(celda.getChildVertical());
                        if(celda.getParentHorizontal() != null){
                            celdaNueva.setParentHorizontal(parentH);
                        }
                        if(celda.getChildHorizontal() !=null){
                            celdaNueva.setChildHorizontal(parentH);
                        }
                        celdaPasoList.add(celdaNueva);
                        //List<Celda> celdas = extract(cars, on(Car.class).getOriginalValue());
                    }
                    contador++;
                }
                columna.setCeldaList(celdaPasoList);
            }
        }
    }
    
    public static void eliminarUnaFila(Grilla grilla, Long filaSelected){    
        for(Columna columna : grilla.getColumnaList()){
            if(columna.getCeldaList()!=null && columna.getCeldaList().size() >= filaSelected.intValue()-1){
                columna.getCeldaList().remove(filaSelected.intValue()-1);        
                Long contador=1L;
                for(Celda celda : columna.getCeldaList()){
                    celda.setIdFila(contador);
                    contador++;
                }
            }
        }
    }
    
    public static boolean deleteRowValidator(Grilla grilla, Long idFila){
            
            boolean validLink = false;
            for(Columna columna : grilla.getColumnaList()){
                if(columna.getCeldaList()!=null && columna.getCeldaList().size() >= idFila.intValue()-1){
                    try{
                        Celda celda = columna.getCeldaList().get(idFila.intValue()-2);
                        if(celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.LINK.getKey())){
                            validLink = true;
                            break;
                        }

                    }catch(IndexOutOfBoundsException e){}
                    catch(Exception e){}
                    
                    try{
                        Celda celda = columna.getCeldaList().get(idFila.intValue());
                        if(celda.getTipoCelda().getIdTipoCelda().equals(TipoCeldaEnum.LINK.getKey())){
                            validLink = true;
                            break;
                        }
                    }catch(IndexOutOfBoundsException e){}
                    catch(Exception e){}
                }
            }
        return validLink;
    }
    
    public static Celda cloneCelda(Celda celda){
        
        Celda newCelda = new Celda();
        
        newCelda.setIdColumna(celda.getIdColumna());
        newCelda.setIdFila(celda.getIdFila());
        newCelda.setIdGrilla(celda.getIdGrilla());
        newCelda.setValor(celda.getValor());
        newCelda.setTipoCelda(celda.getTipoCelda());
        newCelda.setTipoDato(celda.getTipoDato());
        
        /*
        newCelda.setGrupo(celda.getGrupo());
        newCelda.setGrupoResultado(celda.getGrupoResultado());
        newCelda.setFormulaGrillaList(celda.getFormulaGrillaList());
        newCelda.setColumna(celda.getColumna());
        newCelda.setCampoFormulaList(celda.getCampoFormulaList());
        newCelda.setValorLong(celda.getValorLong());
        newCelda.setValorBigDecimal(celda.getValorBigDecimal());
        newCelda.setValorDate(celda.getValorDate());
        newCelda.setEsNumero(celda.isEsNumero());
        newCelda.setEsNumeroYNoTieneGrupo(celda.isEsNumeroYNoTieneGrupo());
        newCelda.setEsTotaloSubTotal(celda.isEsTotaloSubTotal());
        newCelda.setTieneFormulaGrupo(celda.isTieneFormulaGrupo());
        newCelda.setTieneFormulaCelda(celda.isTieneFormulaCelda());*/
        return newCelda;
    }
    
    public static UploadedFile archivoEstructuraValidator(FacesContext facesContext, UploadedFile uploadedFile , FileUpload inputFile) throws Exception{
        uploadedFile.getInputstream();
        if (!uploadedFile.getFileName().endsWith(EXCEL_EXTENSION)) {   
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_excel_detail"),uploadedFile.getFileName()));
            message.setDetail(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_excel_mensaje"));        
            facesContext.addMessage(inputFile.getClientId(facesContext.getCurrentInstance()), message);  
            inputFile.resetValue();             
            inputFile.setValid(Boolean.FALSE);
            return null;
        }
        if (uploadedFile.getSize() > Util.getLong(PropertyManager.getInstance().getMessage("constantes_max_excel_file_bytes"),new Long(0)).longValue()) {
            FacesContext.getCurrentInstance().addMessage((inputFile).getClientId(FacesContext.getCurrentInstance()),
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR, null, PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_excel_maximo_permitido")));
            inputFile.resetValue();
            inputFile.setValid(Boolean.FALSE);
            return null;
        }        
        return uploadedFile;
    }
    
    public static Long getMaxHorizontalParent(Grilla grid){
        long horizontalParent = 0L;
        for(Columna column : grid.getColumnaList()){
            for(Celda cell : column.getCeldaList()){
                if(cell.getParentHorizontal()!=null){
                    if(cell.getParentHorizontal().longValue() > horizontalParent)
                        horizontalParent = cell.getParentHorizontal();
                }
            }
        }
        return horizontalParent + 1L;
    }
    
    public static List<List<Celda>> builHtmlGrilla(List<Columna> columnasE){
		
		List<List<Celda>> celdaDobleList = new ArrayList<List<Celda>>();
		
		int i=0;
		for(Columna columna : columnasE){
			
			for(Celda celda : columna.getCeldaList()){
				
				try{
					
					celdaDobleList.get(i).add(celda);
					
				}catch(IndexOutOfBoundsException e){
					List<Celda> celdas = new ArrayList<Celda>();
					celdas.add(celda);
					celdaDobleList.add(celdas);
				}
				
				i++;
			}
			
			i=0;
		}
		
		return celdaDobleList;
		
	}
    
    
    
   
    public static List<AgrupacionColumnaModelVO> soporteAgrupacionColumna(List<Columna> columnas , List<AgrupacionColumna> agrupaciones) {

        Map<Long, Columna> columnaMap = new LinkedHashMap<Long, Columna>();
        Map<Long, AgrupacionColumnaModelVO> nivel1Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        Map<Long, AgrupacionColumnaModelVO> nivel2Map = new LinkedHashMap<Long, AgrupacionColumnaModelVO>();
        List<AgrupacionColumnaModelVO> nivelesList = new ArrayList<AgrupacionColumnaModelVO>();
        Map<Long,Long> nivel = new HashMap<Long,Long>();
        Long niveles = 0L;
        
        try {
            
            for(Columna columna : columnas){
                columnaMap.put(columna.getIdColumna(), columna);
            }
            
            List<AgrupacionVO> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorVO(agrupaciones);
            
            List<AgrupacionModelVO> agrupacionesModel = new ArrayList<AgrupacionModelVO>();
            
            for(AgrupacionVO agrupacion : agrupacionesNivel){
                
                AgrupacionModelVO agrupacionModel = new AgrupacionModelVO();
                agrupacionModel.setDesde(agrupacion.getDesde());
                agrupacionModel.setHasta(agrupacion.getHasta());
                agrupacionModel.setGrupo(agrupacion.getGrupo());
                agrupacionModel.setAncho(agrupacion.getAncho());
                agrupacionModel.setNivel(agrupacion.getNivel());
                agrupacionModel.setIdGrilla(agrupacion.getIdGrilla());
                agrupacionModel.setTitulo(agrupacion.getTitulo());
                agrupacionesModel.add(agrupacionModel);
            }
            
            
            
            for(AgrupacionModelVO agrupacion : agrupacionesModel){
                
                if(agrupacion.getNivel() == 1L){
                    
                    List<Columna> columnasNew = new ArrayList<Columna>();
                    AgrupacionColumnaModelVO agrupacionN1VO = new AgrupacionColumnaModelVO();
                    agrupacionN1VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN1VO.setNivel(1L);
                    agrupacionN1VO.setAgrupacion(agrupacion);
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        if(columnaMap.containsKey(i)){
                            columnasNew.add(columnaMap.get(i));
                            niveles = 1L;
                        }
                    }
                    agrupacionN1VO.setColumnas(columnasNew);
                    nivel1Map.put(agrupacion.getGrupo(), agrupacionN1VO);
                    
                }else  if(agrupacion.getNivel() == 2L){

                    AgrupacionColumnaModelVO agrupacionN2VO = new AgrupacionColumnaModelVO();
                    
                    List<AgrupacionColumnaModelVO> niveles1New = new ArrayList<AgrupacionColumnaModelVO>();
                    
                    for(Long i=agrupacion.getDesde(); i<= agrupacion.getHasta(); i++){
                        
                        Long grupoNivel1 = getGrupoNivel1(nivel1Map,i);
                        
                        if(grupoNivel1 != null && !nivel.containsKey(grupoNivel1)){
                            nivel.put(grupoNivel1, grupoNivel1);
                            niveles1New.add(nivel1Map.get(grupoNivel1));
                            niveles = 2L;
                        }
                    }
                    
                    agrupacionN2VO.setTituloNivel(agrupacion.getTitulo());
                    agrupacionN2VO.setNivel(2L);
                    agrupacionN2VO.setNiveles(niveles1New);
                    
                    nivel2Map.put(agrupacion.getGrupo(), agrupacionN2VO);
              }
            }
            
            if (niveles == 0L) {
                return null;   
            }
            
            if (niveles == 1L) {
                Iterator it = nivel1Map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                    
                }
            }

            if (niveles == 2L) {
                Iterator it = nivel2Map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry)it.next();
                    nivelesList.add((AgrupacionColumnaModelVO)entry.getValue());
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        
        return nivelesList;
    }
    
    public static List<AgrupacionColumna> getAgrupacionesByColumnaList(List<Columna> columnas){
    	List<AgrupacionColumna> agrupacionColumnas = new ArrayList<AgrupacionColumna>();
    	SortHelper.sortColumnasByOrden(columnas);
    	for(Columna columna : columnas){
    		for(AgrupacionColumna agrupacionColumna : columna.getAgrupacionColumnaList()){
    			agrupacionColumnas.add(agrupacionColumna);
    		}
    	}    	
    	SortHelper.sortAgrupacionColumnaByNivel(agrupacionColumnas);
    	return agrupacionColumnas;
    }
    
    

}
