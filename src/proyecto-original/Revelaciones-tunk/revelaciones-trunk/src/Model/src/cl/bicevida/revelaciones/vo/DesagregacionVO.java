package cl.bicevida.revelaciones.vo;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.ejb.entity.Version;

import java.io.Serializable;


public class DesagregacionVO implements Serializable {

    @SuppressWarnings("compatibility:8878673990316321007")
    private static final long serialVersionUID = -5230878165005533081L;

    public DesagregacionVO() {
        super();
    }
    
    private Catalogo catalogo;
    private Version version;
    private Grilla grilla;
    private SubGrilla subGrilla;
    


    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }

    public void setGrilla(Grilla grilla) {
        this.grilla = grilla;
    }

    public Grilla getGrilla() {
        return grilla;
    }

    public void setSubGrilla(SubGrilla subGrilla) {
        this.subGrilla = subGrilla;
    }

    public SubGrilla getSubGrilla() {
        return subGrilla;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }
}
