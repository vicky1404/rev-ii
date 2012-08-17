package cl.bicevida.revelaciones.ejb.test.reporte;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.w3c.tidy.Tidy;

import org.xhtmlrenderer.pdf.ITextRenderer;

public class TestPDF {
    public TestPDF() {
    }
    public static void main(String[] args) throws Exception{
            String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n" + 
            "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
            "    <head>\n" + 
            "        <title>Hola guataca como estas</title>\n" + 
            "        <style type=\"text/css\"> b { color: blue;} p { font-size: 18px; } </style>\n" + 
            "    </head>\n" + 
            "    <body>\n" + 
            "        <p>\n" + 
            "            <b>HOLA MUNDO... guataca!</b>\n" + 
            "        </p>\n" + 
            "        <p>\n" + 
            "            ...Un sutil pensamiento erroneo puede dar lugar a una indagacion fructifera que revela\n" + 
            "            verdades de gran valor......\n" + 
            "        </p>\n" + 
            "               <table border=\"1\" background=\"red\">\n" + 
            "                       <tr>\n" + 
            "                               <td  style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: windowtext 1pt solid\">Hola</td>\n" + 
            "                               <td>Mundo</td>\n" + 
            "                       </tr>   \n" + 
            "               </table>\n" + 
            "    </body>\n" + 
            "</html>";
            
            // codigo valido para html pero invalido para XHTML
            // no esta cerrando el tag img
            String html1 = new String("<TABLE style=\"WIDTH: 477pt; BORDER-COLLAPSE: collapse\" border=0 cellSpacing=0 cellPadding=0 width=636>\n" + 
            "<COLGROUP>\n" + 
            "<COL style=\"WIDTH: 117pt; mso-width-source: userset; mso-width-alt: 5705\" width=156>\n" + 
            "<COL style=\"WIDTH: 360pt; mso-width-source: userset; mso-width-alt: 17554\" width=480>\n" + 
            "<TBODY>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: windowtext 1pt solid\" class=xl68 height=21 width=156><S><FONT size=2 face=Arial><STRONG>Razón Social</STRONG></FONT></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: windowtext 1pt solid; BORDER-RIGHT: black 1pt solid\" class=xl63 width=480><S><FONT size=2 face=Arial><STRONG>1&nbsp;</STRONG></FONT></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 1\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><FONT size=2 face=Arial><STRONG>Rut</STRONG></FONT></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl64 width=480><S><FONT size=2 face=Arial><STRONG>2321&nbsp;</STRONG></FONT></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 2\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><FONT size=2 face=Arial><STRONG>Domicilio</STRONG></FONT></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl65 width=480><S><FONT size=2 face=Arial><STRONG>321321&nbsp;</STRONG></FONT></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 51.75pt; mso-yfti-irow: 3\" height=69>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 51.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=69 width=156><S><FONT size=2 face=Arial><STRONG>Principales Cambios Societarios de Fusiones y Adquisiciones</STRONG></FONT></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl64 width=480><S><FONT size=2 face=Arial><STRONG>3213&nbsp;</STRONG></FONT></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 4\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><FONT size=2 face=Arial><STRONG>Grupo Económico</STRONG></FONT></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl65 width=480><S><FONT size=2 face=Arial><STRONG>&nbsp;3213</STRONG></FONT></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15pt; mso-yfti-irow: 5\" height=20>\n" + 
            "<TD style=\"BORDER-BOTTOM: black 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 30.75pt; BORDER-TOP: windowtext; BORDER-RIGHT: windowtext 1pt solid\" class=xl70 height=41 rowSpan=2 width=156><STRONG><S><FONT size=2 face=Arial>1.</FONT></S><FONT class=font6 size=1 face=\"Times New Roman\"><S>&nbsp;&nbsp;&nbsp;&nbsp; </S></FONT><FONT class=font5 size=2 face=Arial><S>Nombre de la Entidad Contralora</S></FONT></STRONG></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: black 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: windowtext; BORDER-RIGHT: black 1pt solid\" class=xl66 rowSpan=2 width=480 align=right><S><STRONG>21312123</STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt\" height=21><STRONG></STRONG></TR>\n" + 
            "<TR style=\"HEIGHT: 39pt; mso-yfti-irow: 6\" height=52>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 39pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=52 width=156><S><STRONG><FONT size=2 face=Arial>Nombre de las Controladora última del grupo</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl65 width=480><S><STRONG><FONT size=2 face=Arial>123213&nbsp;</FONT></STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 26.25pt; mso-yfti-irow: 7\" height=35>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 26.25pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=35 width=156><S><STRONG><FONT size=2 face=Arial>Actividades Principales</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl64 width=480><S><STRONG><FONT size=2 face=Arial>213213&nbsp;</FONT></STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 8\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><STRONG><FONT size=2 face=Arial>N° Resolución Exenta</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl65 width=480><S><STRONG><FONT size=2 face=Arial>213213&nbsp;</FONT></STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 26.25pt; mso-yfti-irow: 9\" height=35>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 26.25pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=35 width=156><S><STRONG><FONT size=2 face=Arial>Fecha de Resolución Exenta SVS</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl64 width=480><S><STRONG><FONT size=2 face=Arial>12321&nbsp;</FONT></STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 10\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><STRONG><FONT size=2 face=Arial>N° Registro Valores</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #d0d7e3; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl65 width=480><S><STRONG><FONT size=2 face=Arial>3213&nbsp;</FONT></STRONG></S></TD></TR>\n" + 
            "<TR style=\"HEIGHT: 15.75pt; mso-yfti-irow: 11; mso-yfti-lastrow: yes\" height=21>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: windowtext 1pt solid; BACKGROUND-COLOR: #4a7aaf; WIDTH: 117pt; HEIGHT: 15.75pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: windowtext 1pt solid\" class=xl69 height=21 width=156><S><STRONG><FONT size=2 face=Arial>Accionistas</FONT></STRONG></S></TD>\n" + 
            "<TD style=\"BORDER-BOTTOM: windowtext 1pt solid; BORDER-LEFT: #f0f0f0; BACKGROUND-COLOR: #e9ecf2; WIDTH: 360pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: black 1pt solid\" class=xl64 width=480><S><STRONG><FONT size=2 face=Arial>12321&nbsp;</FONT></STRONG></S></TD></TR></TBODY></TABLE>\n" + 
            "<P>&nbsp;</P>");
            //--
            ByteArrayInputStream b = new ByteArrayInputStream(html1.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
     
            Tidy tidy = new Tidy();
            tidy.setXHTML(true); // queremos que la salida sea xhtml
            tidy.parse(b, out);
            System.out.println(out.toString());
            
            
            TestPDF htmlToPdf=new TestPDF();
            File inputFile = new File("c:\\pdf1.htm");        
            OutputStream os = new FileOutputStream(new File("c:\\pdf1Test.pdf"));

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(out.toString());
            renderer.layout();
            renderer.createPDF(os);
            os.close();
        }
            
}
