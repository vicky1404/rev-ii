package cl.mdr.ifrs.ejb.service.local;

import javax.ejb.Local;
import javax.mail.MessagingException;

@Local
public interface MailServiceLocal {
	
	void sendMail(String asunto, String mensaje, String... para) throws Exception, MessagingException;
}
