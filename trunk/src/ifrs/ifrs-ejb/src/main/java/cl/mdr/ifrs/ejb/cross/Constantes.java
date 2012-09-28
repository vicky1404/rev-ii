package cl.mdr.ifrs.ejb.cross;

/**
 * Clase que posee constantes utilizadas en el Sistema.
 */
public class Constantes {
    
    public static final String PERSISTENCE_UNIT_NAME = "IfrsPU";
    public static final String JDBC_DATASOURCE_NAME = "java:jboss/datasources/ifrsDS";
    public static final String FACES_CONTEXT = "/faces";
    
    public static final String ROL_ADMIN = "IFRS_ROL_ADMINISTRADOR";
    public static final String ROL_ENC = "IFRS_ROL_ENCARGADO";
    public static final String ROL_SUP = "IFRS_ROL_SUPERVISOR";
    public static final String ROL_RESP = "IFRS_ROL_RESPONSABLE";
    public static final String ROL_ROOT = "IFRS_ROL_ROOT";
    
    public static final String GRUPO_ROOT = "GRP_ROOT";
    public static final String GRUPO_ADMIN = "GRP_ADMIN";
    
    
    public static final String PREFIX_CHILD = "CH";
    public static final String PREFIX_PARENT= "PA";
    public static final String PREFIX_CHILD_PARENT = "CP";
    public static final String LIC_FILE_NAME="licence-nr.exf";
    public static final String TYPE_INST="tipo_instalacion";
    public static final String KEYS="ruts";    
    public static final String NUM="numero_ruts";
    public static final String TYPE_INST_CL="cerrada";
    public static final String TYPE_INST_OP="abierta";
    public static final String TYPE_INST_FREE="libre";
}
