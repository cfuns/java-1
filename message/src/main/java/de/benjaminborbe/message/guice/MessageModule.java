package de.benjaminborbe.message.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.message.dao.MessageDaoStorage;
import de.benjaminborbe.message.service.MessageServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MessageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MessageDao.class).to(MessageDaoStorage.class).in(Singleton.class);
		bind(MessageService.class).to(MessageServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
