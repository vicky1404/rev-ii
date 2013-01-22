package cl.mdr.ifrs.modules.reporte.mb;




import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.openpackaging.io.SaveToZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import cl.mdr.ifrs.cross.mb.AbstractBackingBean;
import cl.mdr.ifrs.ejb.entity.Catalogo;
import cl.mdr.ifrs.ejb.entity.Version;
import cl.mdr.ifrs.ejb.reporte.util.SoporteReporte;
import cl.mdr.ifrs.ejb.reporte.vo.PropiedadesReporteVO;
import cl.mdr.ifrs.ejb.reporte.vo.ReportePrincipalVO;

@ManagedBean(name = "reporteUtilBackingBean")
@ViewScoped
public class ReporteUtilBackingBean extends AbstractBackingBean implements Serializable{
	private static final long serialVersionUID = 8047774764060502702L;	    
    private String sufijoNombreReporte = "_".concat(new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date()));

    public ReporteUtilBackingBean() {        
    }
         
    /**
     * @param versiones
     * @return 
     */
    public List<ReportePrincipalVO> getGenerarListReporteVO(final List<Version> versiones) throws Exception{
        if(versiones==null || versiones.size()==0)
            throw new RuntimeException("Error: la lista de reportes no puede ser null");
        
        List<ReportePrincipalVO> reportes = new ArrayList<ReportePrincipalVO>();
        ReportePrincipalVO reporte = null;
        for(Version version : versiones){
            reporte = new ReportePrincipalVO(version, getPropiedades(version.getCatalogo()));
            reportes.add(reporte);
        }       
        return reportes;
    }

	/**
	 * @param catalogo
	 * @return
	 */
	private PropiedadesReporteVO getPropiedades(final Catalogo catalogo) {
		PropiedadesReporteVO propiedades = new PropiedadesReporteVO();		
		propiedades.setNombreHoja(catalogo.getNombre());
		propiedades.setTituloPrincipal(catalogo.getTitulo());
		propiedades.setCatalogo(catalogo);		
		return propiedades;
	}
   
	    
    /**
     * Force the download of the generated Excel file
     * @param xSSFWorkbook
     * @throws Exception
     */
    public void setOuputStreamWorkBook(final XSSFWorkbook xSSFWorkbook) throws Exception {    
        super.getExternalContext().setResponseContentType(SoporteReporte.CONTENT_TYPE_XLS);
        final String fileName = "filename= "+SoporteReporte.NOMBRE_REPORTE.concat(this.getSufijoNombreReporte()).concat(".").concat(SoporteReporte.TIPO_XLSX);
        super.getExternalContext().setResponseHeader("content-disposition","attachment; "+fileName);
        OutputStream outputStream = super.getExternalContext().getResponseOutputStream();
        xSSFWorkbook.write(outputStream);              
        outputStream.flush();
        outputStream.close();        
    }
    
    /**
     * Force the download of the generated Excel file
     * @param xSSFWorkbook
     * @throws Exception
     */
    public void setOuputStreamWorkBook(final XSSFWorkbook xSSFWorkbook, final String fileNameExport) throws Exception {    
        super.getExternalContext().setResponseContentType(SoporteReporte.CONTENT_TYPE_XLS);
        final String fileName = "filename= "+fileNameExport.concat(this.getSufijoNombreReporte()).concat(".").concat(SoporteReporte.TIPO_XLSX);
        super.getExternalContext().setResponseHeader("content-disposition","attachment; "+fileName);
        OutputStream outputStream = super.getExternalContext().getResponseOutputStream();
        xSSFWorkbook.write(outputStream);              
        outputStream.flush();
        outputStream.close();               
    }

    /**
     * Force the download of the generated Word file
     * @param wordMLPackage
     * @throws Exception
     */
    public void setOutPutStreamDocx(final WordprocessingMLPackage wordMLPackage, String nombreReporte) throws Exception {    
    	super.getExternalContext().setResponseContentType(SoporteReporte.CONTENT_TYPE_DOCX);
        final String fileName = "filename= "+nombreReporte;
        super.getExternalContext().setResponseHeader("content-disposition","attachment; "+fileName);
        SaveToZipFile saver = new SaveToZipFile(wordMLPackage);  
        OutputStream outputStream = super.getExternalContext().getResponseOutputStream();
        saver.save(outputStream);
        outputStream.flush();
        outputStream.close();              
    }

    public void setSufijoNombreReporte(String sufijoNombreReporte) {
        this.sufijoNombreReporte = sufijoNombreReporte;
    }

    public String getSufijoNombreReporte() {
        return sufijoNombreReporte;
    }
}
