package cl.bicevida.revelaciones.ejb.facade;


import cl.bicevida.revelaciones.ejb.facade.local.FacadeServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEeffServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEstructuraServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CatalogoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FormulaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.GrillaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.MantenedoresTipoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.PeriodoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ReporteDocxServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ReporteServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.SeguridadServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.VersionServiceLocal;

import cl.bicevida.xbrl.ejb.service.local.TaxonomyLoaderServiceLocal;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class FacadeServiceBean implements FacadeServiceLocal{
    
    public FacadeServiceBean() {
    }   
    
    @EJB private CatalogoServiceLocal catalogoService;
    @EJB private VersionServiceLocal versionService;
    @EJB private MantenedoresTipoServiceLocal MantenedoresTipoService;
    @EJB private PeriodoServiceLocal periodoService;
    @EJB private EstructuraServiceLocal estructuraService;
    @EJB private SeguridadServiceLocal seguridadService;
    @EJB private CeldaServiceLocal celdaService;
    @EJB private GrillaServiceLocal grillaService;
    @EJB private ReporteServiceLocal serviceReporte;
    @EJB private CargadorEstructuraServiceLocal cargadorEstructuraService;
    @EJB private ReporteDocxServiceLocal reporteDocxService;
    @EJB private FormulaServiceLocal formulaService;
    @EJB private EstadoFinancieroServiceLocal estadoFinancieroService;
    @EJB private CargadorEeffServiceLocal cargadorEeffService;
    @EJB private TaxonomyLoaderServiceLocal taxonomyLoaderService; 
    

    public CatalogoServiceLocal getCatalogoService() {
        return catalogoService;
    }

    public VersionServiceLocal getVersionService() {
        return versionService;
    }


    public PeriodoServiceLocal getPeriodoService() {
        return periodoService;
    }
    
    public EstructuraServiceLocal getEstructuraService() {
        return estructuraService;
    }
    
   
    public MantenedoresTipoServiceLocal getMantenedoresTipoService() {
        return MantenedoresTipoService;
    }

    public SeguridadServiceLocal getSeguridadService() {
        return seguridadService;
    }

    public CeldaServiceLocal getCeldaService() {
        return celdaService;
    }

    public GrillaServiceLocal getGrillaService() {
        return grillaService;
    }

    public ReporteServiceLocal getServiceReporte() {
        return serviceReporte;
    }

    public CargadorEstructuraServiceLocal getCargadorEstructuraService() {
        return cargadorEstructuraService;
    }

    public ReporteDocxServiceLocal getReporteDocxService() {
        return reporteDocxService;
    }

    public FormulaServiceLocal getFormulaService() {
        return formulaService;
    }

    public EstadoFinancieroServiceLocal getEstadoFinancieroService() {
        return estadoFinancieroService;
    }

    public CargadorEeffServiceLocal getCargadorEeffService() {
        return cargadorEeffService;
    }

    public TaxonomyLoaderServiceLocal getTaxonomyLoaderService() {
        return taxonomyLoaderService;
    }
}
