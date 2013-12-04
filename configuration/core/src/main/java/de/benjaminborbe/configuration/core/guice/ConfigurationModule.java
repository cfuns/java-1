package de.benjaminborbe.configuration.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.core.dao.ConfigurationDao;
import de.benjaminborbe.configuration.core.dao.ConfigurationDaoStorage;
import de.benjaminborbe.configuration.core.service.ConfigurationGetValue;
import de.benjaminborbe.configuration.core.service.ConfigurationGetValueImpl;
import de.benjaminborbe.configuration.core.service.ConfigurationServiceImpl;
import de.benjaminborbe.configuration.core.service.ConfigurationSetValue;
import de.benjaminborbe.configuration.core.service.ConfigurationSetValueImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class ConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationDao.class).to(ConfigurationDaoStorage.class).in(Singleton.class);
		bind(ConfigurationService.class).to(ConfigurationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(ConfigurationSetValue.class).to(ConfigurationSetValueImpl.class);
		bind(ConfigurationGetValue.class).to(ConfigurationGetValueImpl.class);
	}
}
