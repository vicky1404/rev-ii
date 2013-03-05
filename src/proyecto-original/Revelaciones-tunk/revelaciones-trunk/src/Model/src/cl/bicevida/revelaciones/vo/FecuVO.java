package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.GrupoEeff;

import java.io.Serializable;

import java.util.List;


public class FecuVO implements Serializable {
    
   List<EstadoFinanciero> eeff;
   GrupoEeff grupoEeff;

    public FecuVO(List<EstadoFinanciero> eeff, GrupoEeff grupoEeff) {
        super();
        this.eeff = eeff;
        this.grupoEeff = grupoEeff;
    }

    public void setEeff(List<EstadoFinanciero> eeff) {
        this.eeff = eeff;
    }

    public List<EstadoFinanciero> getEeff() {
        return eeff;
    }

    public void setGrupoEeff(GrupoEeff grupoEeff) {
        this.grupoEeff = grupoEeff;
    }

    public GrupoEeff getGrupoEeff() {
        return grupoEeff;
    }
}
