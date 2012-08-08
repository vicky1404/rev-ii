package cl.mdr.ifrs.ejb.common;

public enum MensajePeriodoEnum {

	
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
    
    private MensajePeriodoEnum(int key, String value){
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
	
	public static MensajePeriodoEnum getMensajeByKey(int key){
		
		MensajePeriodoEnum salida = null;
		
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
