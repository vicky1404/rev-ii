package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.HistorialReporte;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.util.List;

import javax.ejb.Local;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


@Local
public interface ReporteDocxServiceLocal {
    
    WordprocessingMLPackage createDOCX(List<ReportePrincipalVO> reportes, byte[] headerImage, String usuarioOid, String ipUsuario, String nombreArchivo, Periodo periodo) throws Exception;
    
    List<HistorialReporte> findHistorialReporteByPeriodo(Periodo periodo) throws Exception;
    
}
