package cl.mdr.ifrs.ejb.facade.local;


import javax.ejb.Local;


import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyLoaderServiceLocal;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingEstadoFinancieroServiceLocal;
import cl.mdr.exfida.xbrl.ejb.service.local.TaxonomyMappingRevelacionServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CargadorEeffServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CargadorEstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CatalogoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.CeldaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.AreaNegocioServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EmpresaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstadoFinancieroServiceLocal;
import cl.mdr.ifrs.ejb.service.local.EstructuraServiceLocal;
import cl.mdr.ifrs.ejb.service.local.FecuServiceLocal;
import cl.mdr.ifrs.ejb.service.local.FormulaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.GrillaServiceLocal;
import cl.mdr.ifrs.ejb.service.local.GrupoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.MantenedoresTipoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ParametroServiceLocal;
import cl.mdr.ifrs.ejb.service.local.PeriodoServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ReporteDocxServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ReporteServiceLocal;
import cl.mdr.ifrs.ejb.service.local.SeguridadServiceLocal;
import cl.mdr.ifrs.ejb.service.local.VersionServiceLocal;

@Local
public interface FacadeServiceLocal{

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
     * 
     * @return  Interfaz local de EmpresaServiceLocal
     */
    EmpresaServiceLocal getEmpresaService();

    /**
     * @return  Interfaz local de TaxonomyLoaderServiceLocal
     */
    TaxonomyLoaderServiceLocal getTaxonomyLoaderService();
    
   /**
    * @return Interfaz local de FecuServiceLocal
    */
    FecuServiceLocal getFecuService();
    
    /**
     * @return Interfaz local de AreaNegocioServiceLocal
     */
    AreaNegocioServiceLocal getAreaNegocioService();
    
    /**
     * @return Interfaz local de GrupoServiceLocal
     */
    GrupoServiceLocal getGrupoService();
    
    /**
     * @return Interfaz local de ParametroServiceBean
     */
    ParametroServiceLocal getParametroService();
    
    /**
     * 
     * @return Interfaz local de TaxonomyMappingRevelacionServiceBean
     */
    TaxonomyMappingRevelacionServiceLocal getTaxonomyMappingRevelacionService();
    
    /**
     * 
     * @return Interfaz local de TaxonomyMappingEstadoFinancieroServiceBean
     */
    TaxonomyMappingEstadoFinancieroServiceLocal getTaxonomyMappingEstadoFinancieroService();

    
}