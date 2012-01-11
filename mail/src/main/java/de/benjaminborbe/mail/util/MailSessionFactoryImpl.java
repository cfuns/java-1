package de.benjaminborbe.mail.util;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.jndi.InitialContextCache;

@Singleton
public class MailSessionFactoryImpl implements MailSessionFactory {

	private final Logger logger;

	private final InitialContextCache initialContextCache;

	@Inject
	public MailSessionFactoryImpl(final Logger logger, final InitialContextCache initialContextCache) {
		this.logger = logger;
		this.initialContextCache = initialContextCache;
	}

	@Override
	public Session getInstance() throws NamingException {
		logger.debug("getInstance()");
		final Context envCtx = (Context) initialContextCache.lookup("java:comp/env");
		final Session session = (Session) envCtx.lookup("mail/Session");
		return session;
	}

}
