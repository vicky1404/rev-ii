package cl.mdr.ifrs.ejb.service;

import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cl.mdr.ifrs.ejb.service.local.MailServiceLocal;

/**
 * Session Bean implementation class MailServiceBean
 */
@Stateless
public class MailServiceBean implements MailServiceLocal {

    /**
     * Default constructor. 
     */
    public MailServiceBean() {
        super();
    }
    
    public static void main(String args[]){
//    	MailServiceBean mailServiceBean = new MailServiceBean();
//    	try {
//			//mailServiceBean.sendMail("rodrigo.reyesco@gmail.com");
//		} catch (MessagingException e) {			
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
    
    public void sendMailFactory(String... to){
    	
    }
    
    
    private void sendMailExchange(String... to){
    	
    }
    
    private void sendMailSmtp(String... to) throws Exception, MessagingException{
    	
    	String host = "smtp.gmail.com";
    	String from = "contacto@mdrtech.cl";
    	Properties props = System.getProperties();
    	props.put("mail.smtp.starttls.enable", "true"); // added this line
    	props.put("mail.smtp.host", host);
    	props.put("mail.smtp.user", from);
    	props.put("mail.smtp.password", "admin.2010");
    	props.put("mail.smtp.port", "25");
    	props.put("mail.smtp.auth", "true");

    	Session session = Session.getDefaultInstance(props, null);
    	MimeMessage message = new MimeMessage(session);
    	message.setFrom(new InternetAddress("administrador@exfida.com"));    	    
    	for(String toAddress : to){
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
    	}
    	message.setSubject("sending in a group");
    	message.setText("Welcome to JavaMail");
    	// alternately, to send HTML mail:
    	// message.setContent("<p>Welcome to JavaMail</p>", "text/html");
    	Transport transport = session.getTransport("smtp");
    	transport.connect("smtp.gmail.com", "contacto@mdrtech.cl", "admin.2010");
    	transport.sendMessage(message, message.getAllRecipients());
    	transport.close();
    }
    
    

}
