package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.HistorialVersion;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Version;


@Local
public interface PeriodoServiceLocal {
       
    Periodo findPeriodoByPeriodo(Long periodo);
    
   
    Integer abrirPeriodo(String usuario) throws Exception;
    
    /**
     * Retorna el ultimo periodo en estado cerrado.
     * @return
     * @author MDR Technology
     * @throws Exception
     */
    Long findMaxPeriodoCerrado() throws Exception;
    
    
    Integer cerrarPeriodo(PeriodoEmpresa periodoEmpresa, String usuario)  throws Exception;
    
    /**
     * Retorna el �ltimo periodo .
     * @return
     * @author MDR Technology
     * @throws Exception
     */
    Long findMaxPeriodo() throws Exception;

    /**
     * @return
     */
    Periodo findMaxPeriodoObj();

    /**
     * retorna el ultimo periodo iniciado y en contingencia
     * @return
     * @throws Exception
     */
    Periodo findMaxPeriodoIniciado() throws Exception;
    
    /**
     * Persiste un set de versiones con su estado modificado
     * y un registro en el historial para cada una.
     * @param versionList
     * @param historialVersionList
     * @throws Exception
     */
    void persistFlujoAprobacion(List<Version> versionList, List<HistorialVersion> historialVersionList) throws Exception ;
    
    public List<Periodo> findAllPeriodo();
    
    public Periodo findByPeriodo(Long periodo) throws Exception;
    
    public PeriodoEmpresa getMaxPeriodoEmpresaByEmpresa(Long idRut);
    
    public List<PeriodoEmpresa> getMaxPeriodoEmpresaNoCerrado();
    
    public PeriodoEmpresa getPeriodoEmpresaById(Long idPeriodo, Long idRut);

}
