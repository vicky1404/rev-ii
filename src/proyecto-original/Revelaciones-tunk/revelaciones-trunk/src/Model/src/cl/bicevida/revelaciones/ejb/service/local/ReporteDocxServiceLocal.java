package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.HistorialReporte;
import cl.bicevida.revelaciones.ejb.entity.Periodo;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


@Remote
public interface ReporteDocxServiceLocal extends Serializable {
    
    WordprocessingMLPackage createDOCX(List<ReportePrincipalVO> reportes, byte[] headerImage, String usuarioOid, String ipUsuario, String nombreArchivo, Periodo periodo) throws Exception;
    
    List<HistorialReporte> findHistorialReporteByPeriodo(Periodo periodo) throws Exception;
    
}
