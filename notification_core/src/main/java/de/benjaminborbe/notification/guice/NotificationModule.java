package de.benjaminborbe.notification.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.config.NotificationConfig;
import de.benjaminborbe.notification.config.NotificationConfigImpl;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.notification.dao.NotificationMediaDaoStorage;
import de.benjaminborbe.notification.dao.NotificationTypeDao;
import de.benjaminborbe.notification.dao.NotificationTypeDaoStorage;
import de.benjaminborbe.notification.service.NotificationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class NotificationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NotificationMediaDao.class).to(NotificationMediaDaoStorage.class).in(Singleton.class);
		bind(NotificationTypeDao.class).to(NotificationTypeDaoStorage.class).in(Singleton.class);
		bind(NotificationConfig.class).to(NotificationConfigImpl.class).in(Singleton.class);
		bind(NotificationService.class).to(NotificationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
