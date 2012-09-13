package cl.mdr.ifrs.ejb.service.local;

import javax.ejb.Local;

@Local
public interface MailServiceLocal {
	
	void sendMailFactory(String... to);
}
