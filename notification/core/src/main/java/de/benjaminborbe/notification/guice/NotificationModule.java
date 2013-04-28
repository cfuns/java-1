package de.benjaminborbe.notification.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.config.NotificationConfig;
import de.benjaminborbe.notification.config.NotificationConfigImpl;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.notification.dao.NotificationMediaDaoStorage;
import de.benjaminborbe.notification.dao.NotificationTypeDao;
import de.benjaminborbe.notification.dao.NotificationTypeDaoStorage;
import de.benjaminborbe.notification.service.NotificationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class NotificationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(NotificationMediaDao.class).to(NotificationMediaDaoStorage.class).in(Singleton.class);
		bind(NotificationTypeDao.class).to(NotificationTypeDaoStorage.class).in(Singleton.class);
		bind(NotificationConfig.class).to(NotificationConfigImpl.class).in(Singleton.class);
		bind(NotificationService.class).to(NotificationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
