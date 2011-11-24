package de.benjaminborbe.mail.util;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MailSessionFactoryImpl implements MailSessionFactory {

	private final Logger logger;

	@Inject
	public MailSessionFactoryImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Session getInstance() throws NamingException {
		logger.debug("getInstance()");
		final Context initCtx = new InitialContext();
		final Context envCtx = (Context) initCtx.lookup("java:comp/env");
		final Session session = (Session) envCtx.lookup("mail/Session");
		return session;
	}

}
