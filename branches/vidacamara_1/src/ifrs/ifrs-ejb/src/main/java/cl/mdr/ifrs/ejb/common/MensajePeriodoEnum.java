package cl.mdr.ifrs.ejb.common;

public class MensajePeriodoEnum {

	
	public enum CerrarPeriodo{
	
		/*--ERRORES
		--1 SE CIERRA PERIODO SOLO PARA LA EMPRESA
		--2 SE CIERRA PERIODO PARA TODAS LAS EMPRESAS (TODAS LAS EMPRESAS TIENE EL PERIODO CERRADO) 
		--3 EMPERSA NO EXISTE
		--4 ERROR INSERTAR LOG
		--5 PERIODO NO EXISTE
		--6 PERIODO EN ESTADO CERRADO
		--7 OTRO ERROR*/
		
		CIERRA_PERIODO_POR_EMPRESA(1, "Se ha cerrado el periodo para la empresa"),
		CIERRA_PERIODO_TODAS_EMPRESAS(2, "Se ha cerrado el periodo correctamente"),
		EMPRESA_NO_ENCONTRADA(3,"Error, la empresa no se encuentra en base de datos"),
		ERROR_INSERTAR_LOG(4,"Error al insertar el log de la transacción"),
		PERIODO_NO_EXISTE(5,"Error el periodo no existe en base de datos"),
		PERIODO_CERRADO(6,"Error el periodo ya se encuentra en estado cerrado"),
		ERROR_GENERAL(7,"Error al cerrar el periodo");
		
		private int key;
	    private String value;
	    
	    private CerrarPeriodo(int key, String value){
	        this.key = key;
	        this.value = value;
	     }
	
	    public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public static CerrarPeriodo getMensajeByKey(int key){
			
			CerrarPeriodo salida = null;
			
			switch (key) {
			case 1:
				salida = CIERRA_PERIODO_POR_EMPRESA;
				break;
			case 2:
				salida = CIERRA_PERIODO_TODAS_EMPRESAS;
				break;
			case 3:
				salida = EMPRESA_NO_ENCONTRADA;
				break;
			case 4:
				salida = ERROR_INSERTAR_LOG;
				break;
			case 5:
				salida = PERIODO_NO_EXISTE;
				break;
			case 6:
				salida = PERIODO_CERRADO;
				break;
			case 7:
				salida = ERROR_GENERAL;
				break;
	
			default:
				salida = CIERRA_PERIODO_POR_EMPRESA;
				break;
			}
			
			return salida;
		}
	
	}
	
	
	public enum AbrirPeriodo{
		
		/*--ERRORES
		--ERRORES
			--0 ABRE PERIODO CORRECTO
			--2 PERIODO NO CERRADO    
			--3 PERIODO NO CERRADO POR EMPRESA
			--4 INSERTAR LOG
			--5 PERIODO NUEVO EXISTE
			--6 ERROR INSERTAR PERIODO
			--7 ERROR AL GENERAR VERSIONES
			--8 GRILLA INCONSITENTE
			--9 TEXTO INCONSISTENTE
			--10 HTML INCONSISTENTE
			--11 ERROR INESPERADO*/
		
		ABRIR_PERIODO_OK(1, "El período fue abierto de forma exitosa"),
		PERIODO_ESTA_ABIERTO(2, "Se debe cerrar el periodo antes de abrir el período. Todas las empresas deben haber cerrado el período, debido a que se generará un nuevo periodo"),
		PERIODO_EMPRESA_ESTA_ABIERTO(3,"El período de la empresa se encuentra abierto"),
		ERROR_INSERTAR_LOG(4,"Error al insertar el log de la transacción"),
		PERIODO_NO_EXISTE(5,"El período generado exite en base de datos"),
		PERIODO_INSETAR_PERIODO(6,"Error al insertar nuevo período"),
		ERROR_COPIAR_VERSION(7,"Error al copiar información para el nuevo período"),
		ERROR_ESTRUCTURA_GRILLA(8,"Error la estructura grilla que se intenta copiar no existe en base de datos"),
		ERROR_ESTRUCTURA_TEXTO(9,"Error la estructura texto que se intenta copiar no existe en base de datos"),
		ERROR_ESTRUCTURA_HTML(10,"Error la estructura html que se intenta copiar no existe en base de datos"),
		ERROR_FATAL(11,"Error inesperado al copiar información del período anterior");
		
		private int key;
	    private String value;
	    
	    private AbrirPeriodo(int key, String value){
	        this.key = key;
	        this.value = value;
	     }
	
	    public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
		public static AbrirPeriodo getMensajeByKey(int key){
			
			AbrirPeriodo salida = null;
			
			switch (key) {
			case 0:
				salida = ABRIR_PERIODO_OK;
				break;
			case 2:
				salida = PERIODO_ESTA_ABIERTO;
				break;
			case 3:
				salida = PERIODO_EMPRESA_ESTA_ABIERTO;
				break;
			case 4:
				salida = ERROR_INSERTAR_LOG;
				break;
			case 5:
				salida = PERIODO_NO_EXISTE;
				break;
			case 6:
				salida = PERIODO_INSETAR_PERIODO;
				break;
			case 7:
				salida = ERROR_COPIAR_VERSION;
				break;
			case 8:
				salida = ERROR_ESTRUCTURA_GRILLA;
				break;
			case 9:
				salida = ERROR_ESTRUCTURA_TEXTO;
				break;
			case 10:
				salida = ERROR_ESTRUCTURA_HTML;
				break;
	
			default:
				salida = ABRIR_PERIODO_OK;
				break;
			}
			
			return salida;
		}
	
	}
	
}
