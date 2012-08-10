package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Grilla;

import java.util.List;

import javax.ejb.Local;


@Local
public interface GrillaServiceLocal {
    
    Object mergeEntity(Grilla entity);
    
    Object persistEntity(Grilla entity);
    
    Grilla findGrilla(Long idGrilla) throws Exception;
    
    void mergeGrillaList(List<Grilla> grillaList) throws Exception;
    
    Grilla findGrillaById(Long idGrilla) throws Exception;
    
    /**
     * @param grilla
     * @throws Exception
     */
    void persistCell(Grilla grid) throws Exception;
    
    void desagregarGrilla(final Grilla grilla)throws Exception;
    
    void consolidarGrilla(final Grilla grilla)throws Exception;

}
