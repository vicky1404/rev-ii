package cl.mdr.ifrs.ejb.service;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.mdr.ifrs.ejb.common.MailConfigEnum;
import cl.mdr.ifrs.ejb.common.TipoParametroEnum;
import cl.mdr.ifrs.ejb.entity.Parametro;
import cl.mdr.ifrs.ejb.service.local.MailServiceLocal;
import cl.mdr.ifrs.ejb.service.local.ParametroServiceLocal;

/**
 * Session Bean implementation class MailServiceBean
 */
@Stateless
public class MailServiceBean implements MailServiceLocal {

	private static final String TIPO_CONFIG_GOOGLE_APPS = "GOOGLE_APP";
	private static final String TIPO_CONFIG_EXCHANGE = "EXCHANGE";
	private static final String TIPO_CONFIG_SMTP = "SMTP";
			
	@EJB 
	private ParametroServiceLocal parametroService; 
	
	private Map<String, Parametro> mailParams;
	
    /**
     * Default constructor. 
     */
    public MailServiceBean() {
        super();
    }
    
    @PostConstruct
    void init(){
    	this.getMailConfigParams();
    }
    
    private void getMailConfigParams(){
    	if(mailParams == null){
    		this.setMailParams(parametroService.getParametrosConfigMap().get(TipoParametroEnum.MAIL_CONFIG.getKey()));
    	}
    }
            
    public void sendMail(final String asunto, final String mensaje, final String... para) throws Exception, MessagingException{
    	final String tipoSmtp = this.getMailParams().get(MailConfigEnum.TIPO_SMTP.getKey()).getValor();
    	if(tipoSmtp.equals(TIPO_CONFIG_GOOGLE_APPS)){
    		this.sendMailGoogleApps(asunto, mensaje, para);
    	}
    	else if (tipoSmtp.equals(TIPO_CONFIG_EXCHANGE)){
    		this.sendMailExchange(asunto, mensaje,para);
    	}
    	else if (tipoSmtp.equals(TIPO_CONFIG_SMTP)){
    		this.sendMailSmtp(asunto, mensaje,para);
    	}
    }
    
    
    private void sendMailExchange(final String asunto, final String mensaje, final String... para) throws Exception, MessagingException{
    	final String host = this.getMailParams().get(MailConfigEnum.SMTP_HOST.getKey()).getValor();
    	final String user = this.getMailParams().get(MailConfigEnum.SMTP_USER.getKey()).getValor();
    	final String password = this.getMailParams().get(MailConfigEnum.SMTP_PASSWORD.getKey()).getValor();
    	final String port = this.getMailParams().get(MailConfigEnum.SMTP_PORT.getKey()).getValor();
    	final String de = this.getMailParams().get(MailConfigEnum.SMTP_USER.getKey()).getValor();
    	
    	Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", "false");
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(de));        
        for(String toAddress : para){
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
    	}        
        message.setSubject(asunto);
        message.setContent(mensaje, "text/html");
        Transport transport = session.getTransport("smtp");
    	transport.connect(host, user, password);
    	transport.sendMessage(message, message.getAllRecipients());
    	transport.close();
    }
    
    private void sendMailSmtp(final String asunto, final String mensaje, final String... para) throws Exception, MessagingException{
    	
    }
    
    private void sendMailGoogleApps(final String asunto, final String mensaje,final String... para) throws Exception, MessagingException{
    	
    	final String host = this.getMailParams().get(MailConfigEnum.SMTP_HOST.getKey()).getValor();
    	final String user = this.getMailParams().get(MailConfigEnum.SMTP_USER.getKey()).getValor();
    	final String password = this.getMailParams().get(MailConfigEnum.SMTP_PASSWORD.getKey()).getValor();
    	final String port = this.getMailParams().get(MailConfigEnum.SMTP_PORT.getKey()).getValor();
    	final String de = this.getMailParams().get(MailConfigEnum.SMTP_USER.getKey()).getValor();
    	
    	Properties props = System.getProperties();
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.host", host);
    	props.put("mail.smtp.user", user);
    	props.put("mail.smtp.password", password);
    	props.put("mail.smtp.port", port);
    	props.put("mail.smtp.auth", "true");

    	Session session = Session.getDefaultInstance(props, null);
    	
    	MimeMessage message = new MimeMessage(session);
    	message.setFrom(new InternetAddress(de));    	    
    	for(String toAddress : para){
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
    	}
    	
    	message.setSubject(asunto);    	    	
    	message.setContent(mensaje, "text/html");
    	Transport transport = session.getTransport("smtp");
    	transport.connect(host, user, password);
    	transport.sendMessage(message, message.getAllRecipients());
    	transport.close();
    }

	public Map<String, Parametro> getMailParams() {
		return mailParams;
	}

	public void setMailParams(Map<String, Parametro> mailParams) {
		this.mailParams = mailParams;
	}
				
}
