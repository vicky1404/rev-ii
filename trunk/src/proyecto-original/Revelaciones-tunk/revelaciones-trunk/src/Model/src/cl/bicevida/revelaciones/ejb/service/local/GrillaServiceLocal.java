package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Estructura;
import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.ejb.entity.SubGrilla;
import cl.bicevida.revelaciones.ejb.entity.Version;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;


@Remote
public interface GrillaServiceLocal extends Serializable{
    
    Object mergeEntity(Grilla entity);
    
    public Object mergeSubGrilla(SubGrilla entity);
    
    Object persistEntity(Grilla entity);
    
    Grilla findGrilla(Long idGrilla) throws Exception;
    
    void mergeGrillaList(List<Grilla> grillaList) throws Exception;
    
    Grilla findGrillaById(Long idGrilla) throws Exception;
    
    /**
     * @param grilla
     * @throws Exception
     */
    void persistCell(Grilla grid) throws Exception;
    
    void desagregarGrilla(final List<Version> versionList)throws Exception;
    
    void consolidarGrilla(final List<Estructura> estructuraList)throws Exception;
    
    Long getMaxIdSubGrilla() throws Exception;
    
    Long getMaxIdSubColumna() throws Exception;
    
    void eliminarSubGrilla(SubGrilla subGrilla) throws Exception;
    
    void eliminarSubGrillas(List<SubGrilla> subGrillaList) throws Exception;

}
