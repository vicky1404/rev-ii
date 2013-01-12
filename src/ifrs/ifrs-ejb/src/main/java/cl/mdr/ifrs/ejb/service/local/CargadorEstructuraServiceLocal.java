package cl.mdr.ifrs.ejb.service.local;


import java.io.InputStream;

import javax.ejb.Local;

import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.exceptions.CargaGrillaExcelException;
import cl.mdr.ifrs.vo.GrillaVO;


@Local
public interface CargadorEstructuraServiceLocal {
    
    Grilla getGrillaByExcel(InputStream loadedExcel)throws Exception, CargaGrillaExcelException ;
    
    GrillaVO getGrillaVOByExcel(Grilla grilla) throws Exception;
    
    Grilla getGrillaByExcelLoader(final Grilla grid, final InputStream loadedExcel,final Long idGrilla,final Long idFila)throws Exception, CargaGrillaExcelException;
}
