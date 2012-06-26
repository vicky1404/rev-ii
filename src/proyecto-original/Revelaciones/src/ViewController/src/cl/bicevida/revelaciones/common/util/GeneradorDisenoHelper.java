package cl.bicevida.revelaciones.common.util;


import cl.bicevida.revelaciones.ejb.common.TipoCeldaEnum;
import cl.bicevida.revelaciones.ejb.cross.SortHelper;
import cl.bicevida.revelaciones.ejb.entity.AgrupacionColumna;
import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.Columna;
import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.Html;
import cl.bicevida.revelaciones.ejb.entity.Texto;
import cl.bicevida.revelaciones.ejb.entity.TipoEstructura;
import cl.bicevida.revelaciones.ejb.entity.Version;
import cl.bicevida.revelaciones.exceptions.GrillaIncorrectaException;
import cl.bicevida.revelaciones.vo.AgrupacionColumnaModelVO;
import cl.bicevida.revelaciones.vo.AgrupacionModelVO;
import cl.bicevida.revelaciones.vo.AgrupacionVO;
import cl.bicevida.revelaciones.vo.GrillaModelVO;
import cl.bicevida.revelaciones.vo.GrillaVO;
import cl.bicevida.revelaciones.vo.TableModelVO;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import org.apache.myfaces.trinidad.model.UploadedFile;


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
                    List<Grilla>  grillaList = new ArrayList<Grilla>();
                    Grilla grilla = new Grilla();
                    grilla.setEstructura1(estructura);
                    grilla.setColumnaList(grillaModel.getColumnas());
                    grillaList.add(grilla);
                    estructura.addGrilla(grilla);
                    //estructura.setGrillaList(grillaList);
                }else if(estructura.getTipoEstructura().getIdTipoEstructura() == TipoEstructura.ESTRUCTURA_TIPO_TEXTO){
                    List<Texto>   textoList = new ArrayList<Texto>();
                    Texto texto = new Texto();
                    texto.setEstructura2(estructura);
                    textoList.add(texto);
                    estructura.setTextoList(textoList);
                }else{
                    List<Html>    htmlList = new ArrayList<Html>();
                    Html html = new Html();
                    html.setEstructura(estructura);
                    htmlList.add(html);
                    estructura.setHtmlList(htmlList);
                }
            }
        }
        versionVigente.setEstructuraList(estructuras);
        
    }
    
    public static void validarContenidoCelda(Map<Long, GrillaModelVO> grillaModelMap) throws GrillaIncorrectaException, Exception{
        Iterator it = grillaModelMap.entrySet().iterator();
        List<String> mensajes = new ArrayList<String>();
        String mensaje;
        boolean valido = true;
        int i=0;
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            GrillaModelVO grilla = (GrillaModelVO)entry.getValue();
            if(TipoEstructura.ESTRUCTURA_TIPO_GRILLA.equals(grilla.getTipoEstructura())){
                if(grilla.getColumnas()==null || grilla.getColumnas().size()==0)
                    throw new GrillaIncorrectaException("La grilla configurada debe tener al menos una columna");
                for(Columna columna : grilla.getColumnas()){
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
    
    public static List<AgrupacionVO> crearAgrupadorVO(List<AgrupacionColumna> agrupaciones,Long nivel){
        
        List<AgrupacionVO> agrupacionesVO = new ArrayList<AgrupacionVO>();
        
        if(!cl.bicevida.revelaciones.ejb.cross.Util.esListaValida(agrupaciones) || nivel==null)
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
        
        if(!cl.bicevida.revelaciones.ejb.cross.Util.esListaValida(niveles) || agrupacionesMap==null)
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
    
    public static Long getIdCatalogo(final String texto){
        
        Long id = null;
        String string = texto;
        
        try{
            string = string.replaceAll(" ", "");
            if(string.contains("ID:")){
                id = Long.valueOf(string.substring(string.lastIndexOf("ID:")+3,string.length()));
            }
        }catch(Exception e){
            id=null;
        }
        
        return id;
    }
    
    public static Map<Long, GrillaModelVO> createGrillaModel(List<Estructura> estructuras){
        
        Map<Long, GrillaModelVO> grillaModelMap = new LinkedHashMap<Long, GrillaModelVO>();
        
        Long i=1L;
        
        for(Estructura estructura : estructuras){
            
            for (Grilla grilla : estructura.getGrillaList()){
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setTituloGrilla(grilla.getTitulo());
                List<Long> niveles = new ArrayList<Long>();
                Map<Long,List<AgrupacionColumna>> agrupacionesMap = new LinkedHashMap<Long,List<AgrupacionColumna>>();
                for(Columna columna : grilla.getColumnaList()){
                    createAgrupadorMap(columna, niveles, agrupacionesMap, COPY_AGRUPACION);
                }
                grillaModel.setColumnas(grilla.getColumnaList());
                grillaModel.setIdGrilla(grilla.getIdGrilla());
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_GRILLA);
                grillaModel.setNivelesAgregados(niveles);
                grillaModel.setAgrupacionesMap(agrupacionesMap);
                Long cantidadFila = GeneradorDisenoHelper.getContadorFilaByColumnas(grilla.getColumnaList());
                List<Long> filas = new ArrayList<Long>();
                for(Long j=1L; j<=cantidadFila; j++){
                    filas.add(j);
                }
                grillaModel.setFilas(filas);
                grillaModelMap.put(i, grillaModel);
                i++;
            }
            for (Texto texto : estructura.getTextoList()){
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setTexto(texto);
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_TEXTO);
                grillaModelMap.put(i, grillaModel);
                i++;
            }
            
            for (Html html : estructura.getHtmlList()){
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setHtml(html);
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_HTML);
                grillaModelMap.put(i, grillaModel);
                i++;
            } 
        }
        
        return grillaModelMap;
    }
    
    public static RichColumn getRichColumn(String titulo, Long ancho, String alineacion, boolean rowHeader, String id){
        RichColumn column = new RichColumn();
        column.setHeaderText(titulo);
        column.setAlign(alineacion);
        column.setRowHeader(rowHeader);
        column.setRendered(true);
        column.setWidth(ancho+"px;");
        column.setId(id);
        return column;
    }
    
    public static RichOutputText getRichOutputText(ValueExpression value){
        RichOutputText oText = new RichOutputText();
        oText.setValueExpression("value", value);
        oText.setVisible(true);
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
    
    public static Map<Long, GrillaModelVO> cloneGrillaModel(List<Estructura> estructuras){
        
        Map<Long, GrillaModelVO> grillaModelMap = new LinkedHashMap<Long, GrillaModelVO>();
        
        Long i=1L;
        
        for(Estructura estructura : estructuras){
            
            for (Grilla grilla : estructura.getGrillaList()){                
                Grilla grillaNew = new Grilla();
                Map<Long,List<AgrupacionColumna>> agrupacionesMap = new LinkedHashMap<Long,List<AgrupacionColumna>>();
                List<Long> niveles = new ArrayList<Long>();
                grillaNew.setTitulo(grilla.getTitulo());
                grillaNew.setColumnaList(grilla.getColumnaList());
                grillaNew.setEstructura1(grilla.getEstructura1());
                
                GrillaModelVO grillaModel = new GrillaModelVO();
                grillaModel.setTituloGrilla(grillaNew.getTitulo());
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
                    
                    createAgrupadorMap(columna, niveles,agrupacionesMap, CLONE_AGRUPACION);
                    
                    if(columnaNew.getCeldaList()!=null){
                        for(Celda celda : columna.getCeldaList()){
                            celda.setIdGrilla(null);
                        }
                    }
                    
                    columnasNew.add(columnaNew);
                }
                grillaModel.setColumnas(columnasNew);
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_GRILLA);
                Long cantidadFila = GeneradorDisenoHelper.getContadorFilaByColumnas(columnasNew);
                List<Long> filas = new ArrayList<Long>();
                for(Long j=1L; j<=cantidadFila; j++){
                    filas.add(j);
                }
                grillaModel.setFilas(filas);
                grillaModel.setAgrupacionesMap(agrupacionesMap);
                grillaModel.setNivelesAgregados(niveles);
                grillaModelMap.put(i, grillaModel);
                i++;
            }                    
            for (Texto texto : estructura.getTextoList()){                
                GrillaModelVO grillaModel = new GrillaModelVO();
                texto.setIdTexto(null);
                grillaModel.setTexto(texto);
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_TEXTO);
                grillaModelMap.put(i, grillaModel);
                i++;
            }
            
            for (Html html : estructura.getHtmlList()){
                GrillaModelVO grillaModel = new GrillaModelVO();
                html.setIdHtml(null);
                grillaModel.setHtml(html);
                grillaModel.setTipoEstructura(TipoEstructura.ESTRUCTURA_TIPO_HTML);
                grillaModelMap.put(i, grillaModel);
                i++;
            }
        }
        
        return grillaModelMap;
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
            
            List<AgrupacionVO> agrupacionesNivel = GeneradorDisenoHelper.crearAgrupadorVO(agrupaciones, 1L);
            
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
    
    public static void setListaPorNivelUsado(List<AgrupacionColumnaModelVO> agrupacionColumnaList, GrillaVO grilla){
        if(agrupacionColumnaList==null || agrupacionColumnaList.isEmpty()){
            grilla.setNivel(0L);
        }else if(agrupacionColumnaList.get(0).getNivel() == 1L){
            grilla.setNivel(agrupacionColumnaList.get(0).getNivel());
            grilla.setNivel1List(agrupacionColumnaList);
        }else if(agrupacionColumnaList.get(0).getNivel() == 2L){
            grilla.setNivel(agrupacionColumnaList.get(0).getNivel());
            grilla.setNivel2List(agrupacionColumnaList);    
        }
    }
    
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
    
    public static UploadedFile archivoEstructuraValidator(FacesContext facesContext, UploadedFile uploadedFile, RichInputFile inputFile) throws Exception{
        uploadedFile.getInputStream();
        if (!uploadedFile.getFilename().endsWith(EXCEL_EXTENSION)) {   
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(MessageFormat.format(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_excel_detail"),uploadedFile.getFilename()));
            message.setDetail(PropertyManager.getInstance().getMessage("general_mensaje_validacion_upload_excel_mensaje"));        
            facesContext.addMessage(inputFile.getClientId(facesContext.getCurrentInstance()), message);  
            inputFile.resetValue();             
            inputFile.setValid(Boolean.FALSE);
            return null;
        }
        if (uploadedFile.getLength() > cl.bicevida.revelaciones.ejb.cross.Util.getLong(PropertyManager.getInstance().getMessage("constantes_max_excel_file_bytes"),new Long(0)).longValue()) {
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

}
