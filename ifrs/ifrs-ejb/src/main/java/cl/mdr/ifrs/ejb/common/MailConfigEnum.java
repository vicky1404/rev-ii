package cl.mdr.ifrs.ejb.common;

public enum MailConfigEnum {
	
	TIPO_SMTP("TIPO_SMTP"),
	SMTP_HOST("SMTP_HOST"),
	SMTP_USER("SMTP_USER"),
	SMTP_PASSWORD("SMTP_PASSWORD"),
	SMTP_PORT("SMTP_PORT"),
	MAIL_FROM("MAIL_FROM");
	
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
