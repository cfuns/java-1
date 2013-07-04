package de.benjaminborbe.mail.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.naming.NamingException;
import java.util.Properties;

@Singleton
public class MailSessionFactoryMock implements MailSessionFactory {

	@Inject
	public MailSessionFactoryMock() {
	}

	@Override
	public Session getInstance() throws NamingException {
		final Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "zimbra.rp.seibert-media.net");
		props.setProperty("mail.smtp.port", "25");
		final Session session = Session.getInstance(props, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("", "");
			}
		});
		return session;
	}
}
