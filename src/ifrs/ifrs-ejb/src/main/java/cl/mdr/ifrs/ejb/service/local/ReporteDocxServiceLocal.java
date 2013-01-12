package cl.mdr.ifrs.ejb.service.local;

import java.util.List;

import javax.ejb.Local;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import cl.mdr.ifrs.ejb.entity.Empresa;
import cl.mdr.ifrs.ejb.entity.HistorialReporte;
import cl.mdr.ifrs.ejb.entity.Periodo;
import cl.mdr.ifrs.ejb.entity.PeriodoEmpresa;
import cl.mdr.ifrs.ejb.entity.Usuario;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;

@Local
public interface ReporteDocxServiceLocal {
    
    WordprocessingMLPackage createDOCX(List<ReportePrincipalVO> reportes, byte[] headerImage, Usuario usuario, String ipUsuario, String nombreArchivo, PeriodoEmpresa periodoEmpresa) throws Exception;
    
    List<HistorialReporte> findHistorialReporteByPeriodo(Periodo periodo, Empresa empresa) throws Exception;
    
}
