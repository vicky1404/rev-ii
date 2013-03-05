package cl.bicevida.revelaciones.ejb.cross;

public class Constantes {
    
    public static final String PERSISTENCE_UNIT_NAME = "revelacionesPU";
    public static final String JDBC_DATASOURCE_NAME = "java:/app/jdbc/jdbc/RevelacionesDS";
    public static final String FACES_CONTEXT = "/faces";
    
    public static final String ROL_ADMIN = "IFRS_ADMINISTRADOR";
    public static final String ROL_ENC = "IFRS_ENCARGADO";
    public static final String ROL_SUP = "IFRS_SUPERVISOR";
    public static final String ROL_RESP = "IFRS_RESPONSABLE";
    
    public static final String GRUPO_ROOT = "GRP_ROOT";
    public static final String GRUPO_ADMIN = "GRP_ADMIN";
    
    
    public static final String PREFIX_CHILD = "CH";
    public static final String PREFIX_PARENT= "PA";
    public static final String PREFIX_CHILD_PARENT = "CP";
     public static final String MAIL_DOMINIO = "BICEVIDA.CL";
     public static final String MAIL_USER =  "IFRS";
     public static final String MAIL_PASS =  "Ifrs.2012";
     public static final String MAIL_USER_FROM = "ifrs@bicevida.cl";
     public static final String MAIL_HOST =  "CORREO.BICEVIDA.CL";
     public static final String MAIL_PORT =  "25";
     public static final String MAIL_SUBJECT =    "Revelaciones - Carga Estados Financieros";
     public static final String MAIL_SEND = "false";
     public static final String MAIL_USUARIO_TEST = "RODRIGO.DIAZV";
     public static final Boolean MAIL_AUTENTICAR =  Boolean.FALSE;
}
