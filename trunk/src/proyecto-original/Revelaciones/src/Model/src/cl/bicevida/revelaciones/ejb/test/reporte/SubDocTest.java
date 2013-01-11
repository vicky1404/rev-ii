package cl.bicevida.revelaciones.ejb.test.reporte;

import cl.bicevida.revelaciones.ejb.cross.Util;
import cl.bicevida.revelaciones.ejb.entity.Html;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.JAXBElement;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.wml.CTRel;
import org.docx4j.wml.P;
import org.docx4j.wml.P.Hyperlink;
import org.docx4j.wml.R;


public class SubDocTest {
    public SubDocTest() {
        super();
    }
    
    public static String getHtml(){
        StringBuilder html = new StringBuilder();
        html.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + 
        "  <tr>\n" + 
        "    <td colspan=\"3\" align=\"right\"><b style='mso-bidi-font-weight:\n" + 
        "normal'><span style=\"font-size:14.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">FECU (Ficha Estadística Codificada\n" + 
        "  Uniforme) </span></b></td>\n" + 
        "  </tr>\n" + 
        "  <tr>\n" + 
        "    <td colspan=\"3\" align=\"right\"><p><b style='mso-bidi-font-weight:\n" + 
        "normal'><span style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">COMPAÑIAS DEL SEGUNDO GRUPO</span></b></p></td>\n" + 
        "  </tr>\n" + 
        "  <tr>\n" + 
        "    <td>&nbsp;</td>\n" + 
        "    <td>&nbsp;</td>\n" + 
        "    <td>&nbsp;</td>\n" + 
        "  </tr>\n" + 
        "</table>\n" + 
        "<p>&nbsp;</p>\n" + 
        "<div style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">"+                    
        "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">\n" + 
        "  <tr>\n" + 
        "    <td width=\"199\" valign=\"top\"><p>&nbsp;</p></td>\n" + 
        "    <td width=\"399\" colspan=\"2\" valign=\"top\"><p align=\"center\">PERIODO INFORMADO</p></td>\n" + 
        "  </tr>\n" + 
        "  <tr>\n" + 
        "    <td width=\"199\" valign=\"top\"><p>&nbsp;</p></td>\n" + 
        "    <td width=\"200\" valign=\"top\"><p align=\"center\">&nbsp;</p></td>\n" + 
        "    <td width=\"200\" valign=\"top\"><p align=\"center\">&nbsp;</p></td>\n" + 
        "  </tr>\n" + 
        "  <tr>\n" + 
        "    <td width=\"199\" valign=\"top\"><p>&nbsp;</p></td>\n" + 
        "    <td width=\"200\" valign=\"top\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\" align=\"center\"><strong>Fecha de inicio</strong></p></td>\n" + 
        "    <td width=\"200\" valign=\"top\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\" align=\"center\"><strong>Fecha de Termino</strong></p></td>\n" + 
        "  </tr>\n" + 
        "  <tr>\n" + 
        "    <td width=\"199\" valign=\"top\"><p>&nbsp;</p></td>\n" + 
        "    <td width=\"200\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\" align=\"center\">FECHA_INICIO</p></td>\n" + 
        "    <td width=\"200\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\" align=\"center\">FECHA_TERMINO</p></td>\n" + 
        "  </tr>\n" + 
        "</table>\n" + 
        "<p>&nbsp;</p>\n" + 
        "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">\n" + 
        "  <tr>\n" + 
        "    <td width=\"149\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Nombre de Fantasía</strong></p></td>\n" + 
        "    <td width=\"450\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">NOMBRE_FANTASIA</p></td>\n" + 
        "  </tr>\n" + 
        "</table>\n" + 
        "<p>&nbsp;</p>\n" + 
        "<p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>1.- IDENTIFICACION</strong></p>\n" + 
        "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">\n" + 
        "  <tr>\n" + 
        "    <td width=\"100%\" valign=\"top\">\n" + 
        "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"2\"><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"2\"><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Rut</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Razón Social</strong></p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RAZON_SOCIAL</p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Teléfono</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">TELEFONO</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Nombre de Fantasía</strong></p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Casilla</strong><strong> </strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">CASILLA</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">NOMBRE_FANTASIA</p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Fax</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">FAX</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Domicilio Legal</strong></p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>E-Mail</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">EMAIL</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td colspan=\"3\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DOMICILIO_LEGAL</p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Código</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">CODIGO</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td colspan=\"2\"><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Ciudad</strong></p></td>\n" + 
        "        <td colspan=\"2\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">CIUDAD</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Región</strong></p></td>\n" + 
        "        <td colspan=\"2\" style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">REGION</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td colspan=\"2\"><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "    </table></td>\n" + 
        "  </tr>\n" + 
        "</table>\n" + 
        "<br />\n" + 
        "<p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>2.- ADMINISTRACION</strong></p>\n" + 
        "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">\n" + 
        "  <tr>\n" + 
        "    <td width=\"100%\" valign=\"top\">\n" + 
        "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + 
        "      <tr>\n" + 
        "        <td width=\"71%\"><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td width=\"3%\"><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td width=\"26%\"><p><strong>&nbsp;</strong></p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Representante Legal (Apellido Paterno / Materno / Nombre)</strong></p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>RUT Número</strong></p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">REPRESENTANTE_LEGAL</p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_REP</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Gerente General (Apellido Paterno / Materno / Nombre)</strong></p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">GERENTE_GENERAL</p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_GG</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Gerente de Finanzas (Apellido Paterno / Materno / Nombre)</strong></p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">GERENTE_FINANZAS</p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_GF</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Presidente Directorio (Apellido Paterno / Materno / Nombre)</strong></p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td><p>&nbsp;</p></td>\n" + 
        "      </tr>\n" + 
        "      <tr>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">PRES_DIREC</p></td>\n" + 
        "        <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "        <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_PRES_DIC</p></td>\n" + 
        "      </tr>\n" + 
        "    </table>\n" + 
        "      <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" + 
        "        <tr>\n" + 
        "          <td width=\"71%\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><strong>Nombre Directores (Apellido Paterno / Materno / Nombre)</strong></p></td>\n" + 
        "          <td width=\"3%\"><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td width=\"26%\"><p><strong>&nbsp;</strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_1</p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_1</p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_2<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_2<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_3<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_3<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_4<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_4<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_5<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_5<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_6<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_6<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_7<strong></strong></p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_7<strong></strong></p></td>\n" + 
        "        </tr>\n" + 
        "        <tr>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">DIRECTOR_8</p></td>\n" + 
        "          <td><p><strong>&nbsp;</strong></p></td>\n" + 
        "          <td style=\"border:1px solid;\"><p style=\"font-size:10.0pt;mso-bidi-font-size:8.0pt;line-height:\n" + 
        "115%;font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">RUT_DIRECTOR_8<strong></strong></p></td>\n" + 
        "        </tr> \n" + 
        "        <tr>\n" + 
        "               <td>&nbsp;</td>\n" + 
        "            <td>&nbsp;</td>\n" + 
        "            <td>&nbsp;</td>\n" + 
        "        </tr>       \n" + 
        "    </table></td>\n" + 
        "  </tr>\n" + 
        "</table>" +
        "</div>");
        return html.toString();
    }

    public static void main(String[] args) {        
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            String html = getHtml(); 
            AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html")); 
            afiPart.setBinaryData(html.getBytes()); 
            afiPart.setContentType(new ContentType("text/html")); 
            Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart); 
            // .. the bit in document body
            CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk(); 
            ac.setId(altChunkRel.getId() ); 
            wordMLPackage.getMainDocumentPart().addObject(ac); 
            // .. content type
            wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html"); 
            wordMLPackage.save(new java.io.File("c:/test-caratula.docx"));    
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
        
    }

    
}
