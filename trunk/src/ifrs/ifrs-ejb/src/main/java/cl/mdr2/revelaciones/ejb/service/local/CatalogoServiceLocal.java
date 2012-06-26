package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Catalogo;

import cl.bicevida.revelaciones.ejb.entity.Grupo;
import cl.bicevida.revelaciones.ejb.entity.TipoCuadro;

import java.util.List;

import javax.ejb.Local;


@Local
public interface CatalogoServiceLocal {

    Object mergeEntity(Catalogo entity);

    Object persistEntity(Catalogo entity);
    
    List<Catalogo> findCatalogoAll();
    
    List<Catalogo> findCatalogoVigenteAll();
    
    List<Catalogo> findCatalogoNoVigente();
    
    Catalogo findCatalogoByCatalogo(Catalogo catalogo);
    
    void persistEntity(List<Catalogo> lista) throws Exception;
    
    List<Catalogo> findCatalogoByFiltro(String usuario, TipoCuadro tipoCuadro, Grupo grupo, Long vigencia) throws Exception;
    
    List<Catalogo> findAllVigenteByTipo(TipoCuadro tipoCuadro)throws Exception;
    
    List<Catalogo> findAllByTipo(TipoCuadro tipoCuadro, Long vigente)throws Exception;
}
