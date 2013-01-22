package cl.mdr.ifrs.ejb.reporte.vo;

import java.io.Serializable;

public class CargadorEeffVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1957690063577004192L;
	
	int catidadEeffProcesado;
    int catidadEeffDetProcesado;
    
    public CargadorEeffVO(){
    	
    }
    
	public int getCatidadEeffProcesado() {
		return catidadEeffProcesado;
	}
	public void setCatidadEeffProcesado(int catidadEeffProcesado) {
		this.catidadEeffProcesado = catidadEeffProcesado;
	}
	public int getCatidadEeffDetProcesado() {
		return catidadEeffDetProcesado;
	}
	public void setCatidadEeffDetProcesado(int catidadEeffDetProcesado) {
		this.catidadEeffDetProcesado = catidadEeffDetProcesado;
	}

}
