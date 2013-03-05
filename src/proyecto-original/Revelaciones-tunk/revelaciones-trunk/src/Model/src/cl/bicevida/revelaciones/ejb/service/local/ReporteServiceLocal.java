package cl.bicevida.revelaciones.ejb.service.local;


import cl.bicevida.revelaciones.ejb.entity.VersionPeriodo;
import cl.bicevida.revelaciones.ejb.reporte.vo.ReportePrincipalVO;

import java.io.Serializable;

import java.util.List;

import javax.ejb.Remote;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Remote
public interface ReporteServiceLocal extends Serializable{
    
    public XSSFWorkbook createXLSX(List<ReportePrincipalVO> reportes) throws Exception;

    byte[] getBytesHtmlToPdf(byte[] html) throws Exception;
    
    XSSFWorkbook createInterfaceXBRL(List<ReportePrincipalVO> reportes) throws Exception;
    
    XSSFWorkbook createReporteFlujoAprobacion(List<VersionPeriodo> versionPeriodoList) throws Exception;
}
