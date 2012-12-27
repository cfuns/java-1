package de.benjaminborbe.mail.api;

public interface MailService {

	void send(Mail mail) throws MailServiceException;

}
