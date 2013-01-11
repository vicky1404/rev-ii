package cl.bicevida.revelaciones.ejb.facade.local;


import cl.bicevida.revelaciones.ejb.service.local.CaratulaFecuServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEeffServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CargadorEstructuraServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CatalogoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.CeldaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.EstructuraServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FecuServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.FormulaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.GrillaServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.MantenedoresTipoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ParametroServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.PeriodoServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ReporteDocxServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.ReporteServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.SeguridadServiceLocal;
import cl.bicevida.revelaciones.ejb.service.local.VersionServiceLocal;

import cl.bicevida.xbrl.ejb.service.local.TaxonomyLoaderServiceLocal;

import cl.bicevida.xbrl.ejb.service.local.TaxonomyMappingEstadoFinancieroServiceLocal;
import cl.bicevida.xbrl.ejb.service.local.TaxonomyMappingRevelacionServiceLocal;

import java.io.Serializable;

import javax.ejb.Local;

@Local
public interface FacadeServiceLocal {

    /**
     * @return CatalogoServiceLocal
     */
    CatalogoServiceLocal getCatalogoService();
    
    /**
     * @return VersionerviceLocal
     */
    VersionServiceLocal getVersionService();


    /**
     * @return PeriodoServiceLocal
     */
    PeriodoServiceLocal getPeriodoService();

    /**
     * @return EstructuraServiceLocal
     */
    EstructuraServiceLocal getEstructuraService();

    /**
     * @return MantenedoresTipoServiceLocal
     */
    MantenedoresTipoServiceLocal getMantenedoresTipoService();

    /**
     * @return Interfaz local de SeguridadService
     */
    SeguridadServiceLocal getSeguridadService();
    
    /**
     * @return Interfaz local de CeldaServiceLocal
     */
    CeldaServiceLocal getCeldaService();
    
    /**
     * @return Interfaz local de GrillaServiceLocal
     */
    GrillaServiceLocal getGrillaService();
    
    /**
     * @return Interfaz local de ReporteServiceLocal
     */
    ReporteServiceLocal getServiceReporte();

    /**
     * @return Interfaz local de CargadorEstructuraServiceLocal
     */
    CargadorEstructuraServiceLocal getCargadorEstructuraService();

    /**
     * @return Interfaz local de ReporteDocxServiceLocal
     */
    ReporteDocxServiceLocal getReporteDocxService();

    /**
     * @return Interfaz local de FormulaServiceLocal (nuevo metodo para formulas)
     */
    FormulaServiceLocal getFormulaService();
    
    /**
     * @return Interfaz local de EstadoFinancieroServiceLocal
     */
    EstadoFinancieroServiceLocal getEstadoFinancieroService();
    
    /**
     * @return Interfaz local de CargadorEeffServiceLocal
     */
    CargadorEeffServiceLocal getCargadorEeffService();
    
    /**
     * @return Interfaz local de TaxonomyLoaderService
     */
    TaxonomyLoaderServiceLocal getTaxonomyLoaderService();

    /**
     * @return Interfaz local de TaxonomyMappingRevelacionService
     */
    TaxonomyMappingRevelacionServiceLocal getTaxonomyMappingRevelacionService();

    /**
     * @return Interfaz local de TaxonomyMappingEstadoFinancieroService
     */
    TaxonomyMappingEstadoFinancieroServiceLocal getTaxonomyMappingEstadoFinancieroService();
    
    /**
     * @return Interfaz local de FecuService
     */
    FecuServiceLocal getFecuService();

    /**
     * @return
     */
    CaratulaFecuServiceLocal getCaratulaFecuService();

    /**
     * @return
     */
    public ParametroServiceLocal getParametroService();
    
    
    
}
