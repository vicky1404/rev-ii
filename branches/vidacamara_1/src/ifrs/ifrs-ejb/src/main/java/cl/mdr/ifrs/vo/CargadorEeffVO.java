package cl.mdr.ifrs.vo;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cl.mdr.ifrs.ejb.entity.DetalleEeff;
import cl.mdr.ifrs.ejb.entity.EstadoFinanciero;
import cl.mdr.ifrs.ejb.entity.RelacionDetalleEeff;
import cl.mdr.ifrs.ejb.entity.RelacionEeff;
import cl.mdr.ifrs.ejb.entity.UsuarioGrupo;

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
    
    private Map<Long,Long> grillaNoValida;
    
    

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
    
    public Map<Long, Long> getGrillaNoValida() {
		return grillaNoValida;
	}

	public void setGrillaNoValida(Map<Long, Long> grillaNoValida) {
		this.grillaNoValida = grillaNoValida;
	}
}

