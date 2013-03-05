package cl.bicevida.revelaciones.ejb.reporte.util;


import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;
import cl.bicevida.revelaciones.ejb.entity.VersionEeff;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.R;


public class CuadroEeffHelper {
    public CuadroEeffHelper() {
        super();
    }
    
    public static String getCuadroEeffHtml(VersionEeff versionEeff) {
        StringBuffer html = new StringBuffer();
        String style="";  
        String bgcolor="";  
        Long grupoAux = null;       
        
        List<EstadoFinanciero> estadoFinancieroList = versionEeff.getEstadoFinancieroList();
        
        Collections.sort(estadoFinancieroList, new Comparator<EstadoFinanciero>(){
            
         public int compare(EstadoFinanciero ef1, EstadoFinanciero ef2) {
                int result = 0;
             
          if (ef1.getCodigoFecu() != null && ef1.getCodigoFecu().getGrupoEeff() != null && ef1.getCodigoFecu().getGrupoEeff().getOrden() != null &&
              ef2.getCodigoFecu() != null && ef2.getCodigoFecu().getGrupoEeff() != null && ef2.getCodigoFecu().getGrupoEeff().getOrden() != null ) {  
              
             result = ef1.getCodigoFecu().getGrupoEeff().getOrden().compareTo(ef2.getCodigoFecu().getGrupoEeff().getOrden());
             if (result != 0){
                    return result;
                 }
             
            result = ef1.getCodigoFecu().getGrupoEeff().getIdGrupoEeff().compareTo(ef2.getCodigoFecu().getGrupoEeff().getIdGrupoEeff());
            
                if (result != 0){
                       return result;
                    }
         }     
         
        if (ef1.getCodigoFecu() != null && ef1.getCodigoFecu().getOrden() != null && ef2.getCodigoFecu() != null && ef2.getCodigoFecu().getOrden() != null)   {
            
             result = ef1.getCodigoFecu().getIdFecu().compareTo(ef2.getCodigoFecu().getIdFecu());
             
             if (result != 0){
                    return result;
                 }
            
                result = ef1.getCodigoFecu().getOrden().compareTo(ef2.getCodigoFecu().getOrden());
                
                if (result != 0){
                    return result;
                 }
            
         }
                
                return  result;
            }                            
         
            });
        
           
        
            for (EstadoFinanciero eeff : estadoFinancieroList){
                
                if (eeff.getCodigoFecu().isBold()){ //Pregunta si el campo es en negrita
                        style = "style=\"font-weight:bold;font-size:10px\""; //Agrega el estilo negrita a la tabla
                        bgcolor="bgcolor=\"#EBF1DE\"";
                } else {
                        style  = "style=\"font-size:10px\"";
                        bgcolor = "";
                    }
                
               

                if (eeff.getCodigoFecu() != null && eeff.getCodigoFecu().getGrupoEeff() != null && eeff.getCodigoFecu().getGrupoEeff().isVigencia() && eeff.getCodigoFecu().getGrupoEeff().getIdGrupoEeff() != null && (!eeff.getCodigoFecu().getGrupoEeff().getIdGrupoEeff().equals(grupoAux) || grupoAux == null )){                    
                    
                    html.append("<div style=\"font:Arial, Helvetica, sans-serif;\">");
                    
                    if (grupoAux != null){
                        
                            html.append("</table>\n");
                            html.append("</div>\n");
                            html.append("<br/>\n");
                    }
                    
                    html.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse;border:1px solid #c3c3c3;font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n");
                    html.append("115%;font-family:Arial\">\n");
                    
                    html.append("  <tr bgcolor=\"#99CC66\">\n");
                    html.append("    <td width=\"80%\" colspan=\"3\" style=\"font-weight:bold;font-size:15px\" valign=\"middle\" align=\"left\">" + eeff.getCodigoFecu().getGrupoEeff().getDescripcion().toUpperCase() +" </td>");                    
                    html.append("    <td width=\"20%\" valign=\"middle\" style=\"font-weight:bold;font-size:15px\" align=\"left\">").append( eeff.getVersionEeff().getPeriodo().getPeriodoFormat() ).append("</td>");                    
                    html.append("  </tr>\n");
                    
                    grupoAux = eeff.getCodigoFecu().getGrupoEeff().getIdGrupoEeff();
                }
                
                    if (eeff.getCodigoFecu() != null && eeff.getCodigoFecu().getGrupoEeff() != null && eeff.getCodigoFecu().getGrupoEeff().isVigencia()){
                    
                            html.append("  <tr>\n");
                            html.append("    <td width=\"20%\" bgcolor=\"#EBF1DE\" ").append(style).append  (" valign=\"middle\">").append( Util.getLong(eeff.getIdFecu(), 0L) ).append("</td>");                    
                            html.append("    <td width=\"20%\" bgcolor=\"#EBF1DE\" ").append(style).append  (" valign=\"middle\">").append( Util.getString(eeff.getFecuFormat(), "")).append("</td>");
                            html.append("    <td width=\"40%\" bgcolor=\"#EBF1DE\" ").append(style).append  (" valign=\"middle\">");
                                if (eeff.getCodigoFecu().isSubGrupo()){
                                        html.append("&nbsp;&nbsp;&nbsp;");
                                    }
                            html.append( Util.getString(eeff.getCodigoFecu().getDescripcion() , "")).append("</td>");
                            html.append("    <td width=\"20%\" ").append(style).append(" ").append(bgcolor).append  (" valign=\"middle\" align=\"right\">").append(  Util.getDecimalFormat().format(Util.getBigDecimal(eeff.getMontoTotal(), new BigDecimal(0))) ).append("</td>");
                            html.append("  </tr>\n");
                    }
                
                    
                
                }
            
        System.err.println(html.toString());
        return html.toString();
    }
    
    public static void createCuadroEEFF(WordprocessingMLPackage wordMLPackage, R run,  VersionEeff versionEeff) throws JAXBException,
                                                                                               InvalidFormatException {
        AlternativeFormatInputPart afiPart = null;
        Relationship altChunkRel = null;
        CTAltChunk ac = null;
        afiPart = new AlternativeFormatInputPart(new PartName("/hw-eeff.html"));
        afiPart.setBinaryData(getCuadroEeffHtml(versionEeff).getBytes());
        afiPart.setContentType(new ContentType("text/html"));
        altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);
        ac = Context.getWmlObjectFactory().createCTAltChunk();
        ac.setId(altChunkRel.getId());
        run.getContent().add(ac);
        wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
    }
}
