package de.benjaminborbe.websearch.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.core.config.WebsearchConfig;
import de.benjaminborbe.websearch.core.config.WebsearchConfigImpl;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationDaoStorage;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDaoStorage;
import de.benjaminborbe.websearch.core.service.WebsearchServiceImpl;
import de.benjaminborbe.websearch.core.util.WebsearchUpdateDeterminer;
import de.benjaminborbe.websearch.core.util.WebsearchUpdateDeterminerImpl;
import org.slf4j.Logger;

public class WebsearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WebsearchConfig.class).to(WebsearchConfigImpl.class).in(Singleton.class);
		bind(WebsearchService.class).to(WebsearchServiceImpl.class).in(Singleton.class);
		bind(WebsearchUpdateDeterminer.class).to(WebsearchUpdateDeterminerImpl.class).in(Singleton.class);
		bind(WebsearchConfigurationDao.class).to(WebsearchConfigurationDaoStorage.class).in(Singleton.class);
		bind(WebsearchPageDao.class).to(WebsearchPageDaoStorage.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(WebsearchValidatorLinker.class);
	}
}
