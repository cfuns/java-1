package de.benjaminborbe.proxy.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfigImpl;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDaoCache;
import de.benjaminborbe.proxy.core.service.ProxyCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class ProxyCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(ProxyCoreConfig.class).to(ProxyCoreConfigImpl.class).in(Singleton.class);
		bind(ProxyService.class).to(ProxyCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(ProxyCoreConversationDao.class).to(ProxyCoreConversationDaoCache.class).in(Singleton.class);
	}
}
