package cl.mdr.ifrs.ejb.facade;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import cl.mdr.ifrs.ejb.facade.local.FacadeServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CargadorEeffServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CargadorEstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CatalogoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CeldaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.FormulaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.GrillaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.MantenedoresTipoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.PeriodoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ReporteDocxServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ReporteServiceLocal;
import cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal;
import cl.mdr.ifrs.ejb.service.local.VersionServiceLocal;

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
}
