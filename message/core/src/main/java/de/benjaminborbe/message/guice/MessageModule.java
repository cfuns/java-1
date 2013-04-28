package de.benjaminborbe.message.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.config.MessageConfig;
import de.benjaminborbe.message.config.MessageConfigImpl;
import de.benjaminborbe.message.dao.MessageDao;
import de.benjaminborbe.message.dao.MessageDaoStorage;
import de.benjaminborbe.message.service.MessageServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class MessageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(MessageConfig.class).to(MessageConfigImpl.class).in(Singleton.class);
		bind(MessageDao.class).to(MessageDaoStorage.class).in(Singleton.class);
		bind(MessageService.class).to(MessageServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(MessageValidatorLinker.class);
	}
}
