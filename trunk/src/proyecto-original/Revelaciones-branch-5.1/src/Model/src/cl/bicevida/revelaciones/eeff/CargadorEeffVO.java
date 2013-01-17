package cl.bicevida.revelaciones.eeff;

import cl.bicevida.revelaciones.ejb.entity.Celda;
import cl.bicevida.revelaciones.ejb.entity.DetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;

import cl.bicevida.revelaciones.ejb.entity.RelacionDetalleEeff;
import cl.bicevida.revelaciones.ejb.entity.RelacionEeff;

import cl.bicevida.revelaciones.ejb.entity.UsuarioGrupo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class CargadorEeffVO implements Serializable{


    @SuppressWarnings("compatibility:-3678620716433083209")
    private static final long serialVersionUID = -908755655628682600L;
    
    private List<EstadoFinanciero> eeffList;
    
    List<EstadoFinanciero>  eeffDescuadreList;
    List<DetalleEeff> eeffDetDescuadreList;
    
    List<RelacionEeff>  relEeffDescuadreList;
    List<RelacionDetalleEeff> relEeffDetDescuadreList;
    
    List<EstadoFinanciero>  eeffBorradoList;
    List<DetalleEeff> eeffDetBorradoList;
    
    List<RelacionEeff>  relEeffBorradoList;
    List<RelacionDetalleEeff> relEeffDetBorradoList;
    
    List<UsuarioGrupo> usuarioGrupoList;
    
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
}
