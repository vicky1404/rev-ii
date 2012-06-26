package cl.bicevida.revelaciones.ejb.reporte.vo;


import cl.bicevida.revelaciones.ejb.entity.Version;

import java.io.Serializable;


public class ReportePrincipalVO implements Serializable{

    private static final long serialVersionUID = 1L;
    public static final Integer TIPO_XLS = Integer.valueOf(1);
    public static final Integer TIPO_PDF = Integer.valueOf(2);
    private PropiedadesReporteVO propiedades;
    private Version version;
    
    
    public ReportePrincipalVO() {
    }
    
    public ReportePrincipalVO(Version version,PropiedadesReporteVO propiedades) {
        this.version = version;
        this.propiedades = propiedades;
    }

    public void setPropiedades(PropiedadesReporteVO propiedades) {
        this.propiedades = propiedades;
    }

    public PropiedadesReporteVO getPropiedades() {
        return propiedades;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }
}
