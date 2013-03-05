package cl.bicevida.revelaciones.eeff;


import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;
import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CargadorEeffVO implements Serializable{


    @SuppressWarnings("compatibility:-3678620716433083209")
    private static final long serialVersionUID = -908755655628682600L;
    
    private List<EstadoFinanciero> eeffList;
    
    private List<EstadoFinanciero>  eeffDescuadreList;
    private List<DetalleEeff> eeffDetDescuadreList;
    
    private List<RelacionEeff>  relEeffDescuadreList;
    private List<RelacionDetalleEeff> relEeffDetDescuadreList;
    
    private List<EstadoFinanciero>  eeffBorradoList;
    private List<DetalleEeff> eeffDetBorradoList;
    
    private List<RelacionEeff>  relEeffBorradoList;
    private List<RelacionDetalleEeff> relEeffDetBorradoList;
    
    private Map<Long,Long> grillaNoValida;
    
    private List<UsuarioGrupo> usuarioGrupoList;
    
    private int catidadEeffProcesado = 0;
    private int catidadEeffDetProcesado = 0;
    
    public CargadorEeffVO() {
        super();
    }

    public void setEeffList(List<EstadoFinanciero> eeffList) {
        this.eeffList = eeffList;
    }

    public List<EstadoFinanciero> getEeffList() {
        return eeffList;
    }

    public void setEeffDescuadreList(List<EstadoFinanciero> eeffDescuadreList) {
        this.eeffDescuadreList = eeffDescuadreList;
    }

    public List<EstadoFinanciero> getEeffDescuadreList() {
        return eeffDescuadreList;
    }

    public void setEeffDetDescuadreList(List<DetalleEeff> eeffDetDescuadreList) {
        this.eeffDetDescuadreList = eeffDetDescuadreList;
    }

    public List<DetalleEeff> getEeffDetDescuadreList() {
        return eeffDetDescuadreList;
    }

    public void setRelEeffDescuadreList(List<RelacionEeff> relEeffDescuadreList) {
        this.relEeffDescuadreList = relEeffDescuadreList;
    }

    public List<RelacionEeff> getRelEeffDescuadreList() {
        return relEeffDescuadreList;
    }

    public void setRelEeffDetDescuadreList(List<RelacionDetalleEeff> relEeffDetDescuadreList) {
        this.relEeffDetDescuadreList = relEeffDetDescuadreList;
    }

    public List<RelacionDetalleEeff> getRelEeffDetDescuadreList() {
        return relEeffDetDescuadreList;
    }

    public void setEeffBorradoList(List<EstadoFinanciero> eeffBorradoList) {
        this.eeffBorradoList = eeffBorradoList;
    }

    public List<EstadoFinanciero> getEeffBorradoList() {
        return eeffBorradoList;
    }

    public void setEeffDetBorradoList(List<DetalleEeff> eeffDetBorradoList) {
        this.eeffDetBorradoList = eeffDetBorradoList;
    }

    public List<DetalleEeff> getEeffDetBorradoList() {
        return eeffDetBorradoList;
    }

    public void setRelEeffBorradoList(List<RelacionEeff> relEeffBorradoList) {
        this.relEeffBorradoList = relEeffBorradoList;
    }

    public List<RelacionEeff> getRelEeffBorradoList() {
        return relEeffBorradoList;
    }

    public void setRelEeffDetBorradoList(List<RelacionDetalleEeff> relEeffDetBorradoList) {
        this.relEeffDetBorradoList = relEeffDetBorradoList;
    }

    public List<RelacionDetalleEeff> getRelEeffDetBorradoList() {
        return relEeffDetBorradoList;
    }

    public void setCatidadEeffProcesado(int catidadEeffProcesado) {
        this.catidadEeffProcesado = catidadEeffProcesado;
    }

    public int getCatidadEeffProcesado() {
        return catidadEeffProcesado;
    }

    public void setCatidadEeffDetProcesado(int catidadEeffDetProcesado) {
        this.catidadEeffDetProcesado = catidadEeffDetProcesado;
    }

    public int getCatidadEeffDetProcesado() {
        return catidadEeffDetProcesado;
    }

    public void setUsuarioGrupoList(List<UsuarioGrupo> usuarioGrupoList) {
        this.usuarioGrupoList = usuarioGrupoList;
    }

    public List<UsuarioGrupo> getUsuarioGrupoList() {
        return usuarioGrupoList;
    }

    public void setGrillaNoValida(Map<Long, Long> grillaNoValida) {
        this.grillaNoValida = grillaNoValida;
    }

    public Map<Long, Long> getGrillaNoValida() {
        if(grillaNoValida==null){
            grillaNoValida = new LinkedHashMap<Long, Long>();
        }
        return grillaNoValida;
    }
}

