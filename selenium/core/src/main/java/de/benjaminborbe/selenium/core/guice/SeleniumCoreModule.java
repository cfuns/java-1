package de.benjaminborbe.selenium.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.selenium.core.config.SeleniumCoreConfigImpl;
import de.benjaminborbe.selenium.core.service.SeleniumCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SeleniumCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(SeleniumCoreConfig.class).to(SeleniumCoreConfigImpl.class).in(Singleton.class);
		bind(SeleniumService.class).to(SeleniumCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
