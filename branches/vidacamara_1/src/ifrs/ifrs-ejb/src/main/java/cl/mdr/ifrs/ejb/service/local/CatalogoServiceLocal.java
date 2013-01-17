package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Grupo;
import cl.mdr.ifrs.ejb.entity.TipoCuadro;


@Local
public interface CatalogoServiceLocal {

    Object mergeEntity(Catalogo entity);

    Object persistEntity(Catalogo entity);
    
    List<Catalogo> findCatalogoAll();
    
    List<Catalogo> findCatalogoVigenteAll();
    
    List<Catalogo> findCatalogoNoVigente();
    
    Catalogo findCatalogoByCatalogo(Catalogo catalogo);
    
    void persistEntity(List<Catalogo> lista) throws Exception;
    
    List<Catalogo> findCatalogoByFiltro(Long rutEmpresa, String usuario, TipoCuadro tipoCuadro, Grupo grupo, Long vigencia) throws Exception;
    
    List<Catalogo> findCatalogoByGrupo(Long rutEmpresa, TipoCuadro tipoCuadro, Grupo grupo, Long vigencia) throws Exception;
    
    List<Catalogo> findAllVigenteByTipo(Long rutEmpresa, TipoCuadro tipoCuadro)throws Exception;
    
    List<Catalogo> findAllByTipo(Long tipoCuadro, Long vigente, Long idRut) throws Exception;
}