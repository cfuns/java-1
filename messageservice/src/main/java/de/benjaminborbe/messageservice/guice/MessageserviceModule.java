package de.benjaminborbe.messageservice.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.MessageService;
import de.benjaminborbe.messageservice.dao.MessageDao;
import de.benjaminborbe.messageservice.dao.MessageDaoStorage;
import de.benjaminborbe.messageservice.service.MessageServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MessageserviceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MessageDao.class).to(MessageDaoStorage.class).in(Singleton.class);
		bind(MessageService.class).to(MessageServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
