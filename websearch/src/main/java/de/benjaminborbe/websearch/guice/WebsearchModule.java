package de.benjaminborbe.websearch.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.websearch.page.PageDao;
import de.benjaminborbe.websearch.page.PageDaoImpl;

public class WebsearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PageDao.class).to(PageDaoImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
