package de.benjaminborbe.websearch.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.configuration.ConfigurationDao;
import de.benjaminborbe.websearch.configuration.ConfigurationDaoImpl;
import de.benjaminborbe.websearch.page.PageDao;
import de.benjaminborbe.websearch.page.PageDaoImpl;
import de.benjaminborbe.websearch.service.WebsearchServiceImpl;
import de.benjaminborbe.websearch.util.UpdateDeterminer;
import de.benjaminborbe.websearch.util.UpdateDeterminerImpl;

public class WebsearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WebsearchService.class).to(WebsearchServiceImpl.class).in(Singleton.class);
		bind(UpdateDeterminer.class).to(UpdateDeterminerImpl.class).in(Singleton.class);
		bind(ConfigurationDao.class).to(ConfigurationDaoImpl.class).in(Singleton.class);
		bind(PageDao.class).to(PageDaoImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
