package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.Grilla;
import cl.bicevida.revelaciones.exceptions.CargaGrillaExcelException;
import cl.bicevida.revelaciones.vo.GrillaVO;

import java.io.InputStream;

import javax.ejb.Local;


@Local
public interface CargadorEstructuraServiceLocal {
    
    Grilla getGrillaByExcel(InputStream loadedExcel)throws Exception, CargaGrillaExcelException ;
    
    GrillaVO getGrillaVOByExcel(Grilla grilla) throws Exception;
    
    Grilla getGrillaByExcelLoader(final Grilla grid, final InputStream loadedExcel,final Long idGrilla,final Long idFila)throws Exception, CargaGrillaExcelException;
}
