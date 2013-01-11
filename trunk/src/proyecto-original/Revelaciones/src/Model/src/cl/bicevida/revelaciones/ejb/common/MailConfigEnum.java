package cl.bicevida.revelaciones.ejb.common;

public enum MailConfigEnum {
	
	CARGADOR_MAIL_FROM("CARGADOR_MAIL_FROM"),
        CARGADOR_MAIL_PASS_FROM("CARGADOR_MAIL_PASS_FROM"),
	MAIL_HOST("MAIL_HOST"),
        MAIL_PORT("MAIL_PORT"),
	CARGADOR_SUBJECT("CARGADOR_SUBJECT"),
	ENVIAR_EMAIL("ENVIAR_EMAIL"),
	MAIL_USUARIO_TEST("MAIL_USUARIO_TEST"),
        MAIL_DOMINIO("MAIL_DOMINIO"),
        MAIL_AUTENTUCAR("MAIL_AUTENTICAR");
	
	private String key;
	
	private MailConfigEnum(String key){            
            this.key = key;
	}

	public String getKey() {
            return key;
	}

	public void setKey(String key) {
            this.key = key;
	}				

}