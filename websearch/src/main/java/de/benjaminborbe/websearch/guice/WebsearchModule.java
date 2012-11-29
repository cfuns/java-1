package de.benjaminborbe.websearch.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationDaoStorage;
import de.benjaminborbe.websearch.page.WebsearchPageDao;
import de.benjaminborbe.websearch.page.WebsearchPageDaoStorage;
import de.benjaminborbe.websearch.service.WebsearchServiceImpl;
import de.benjaminborbe.websearch.util.WebsearchUpdateDeterminer;
import de.benjaminborbe.websearch.util.WebsearchUpdateDeterminerImpl;

public class WebsearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WebsearchService.class).to(WebsearchServiceImpl.class).in(Singleton.class);
		bind(WebsearchUpdateDeterminer.class).to(WebsearchUpdateDeterminerImpl.class).in(Singleton.class);
		bind(WebsearchConfigurationDao.class).to(WebsearchConfigurationDaoStorage.class).in(Singleton.class);
		bind(WebsearchPageDao.class).to(WebsearchPageDaoStorage.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
