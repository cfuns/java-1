package de.benjaminborbe.mail.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.service.MailServiceImpl;
import de.benjaminborbe.mail.util.MailSessionFactory;
import de.benjaminborbe.mail.util.MailSessionFactoryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MailModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MailService.class).to(MailServiceImpl.class).in(Singleton.class);
		bind(MailSessionFactory.class).to(MailSessionFactoryImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
