package cl.mdr.ifrs.ejb.service.local;


import java.util.List;

import javax.ejb.Local;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.mdr.ifrs.ejb.entity.Grilla;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;


@Local
public interface ReporteServiceLocal {
    
	XSSFWorkbook createXLSXOneGrid(Grilla grilla, String tituloHoja, boolean formatoPesos, boolean formatoYYYYMMDD)  throws Exception;
    XSSFWorkbook createXLSX(List<ReportePrincipalVO> reportes, boolean formatoPesos, boolean formatoYYYYMMDD) throws Exception;
    XSSFWorkbook createXLSXXbrl(List<ReportePrincipalVO> reportes, boolean formatoPesos, boolean formatoYYYYMMDD) throws Exception;       
    XSSFWorkbook createInterfaceXBRL(List<ReportePrincipalVO> reportes) throws Exception;    
    XSSFWorkbook createReporteFlujoAprobacion(List<Version> versionPeriodoList) throws Exception;
}
