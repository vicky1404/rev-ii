package cl.mdr.ifrs.ejb.service.local;

import java.util.Map;

import javax.ejb.Local;
import javax.mail.MessagingException;

import cl.mdr.ifrs.ejb.entity.Parametro;

@Local
public interface MailServiceLocal {
	
	void sendMail(String asunto, String mensaje, String... para) throws Exception, MessagingException;
	
	Map<String, Parametro> getMailParams();
}
