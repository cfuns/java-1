package de.benjaminborbe.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.BookmarkDao;
import de.benjaminborbe.index.dao.BookmarkDaoImpl;
import de.benjaminborbe.index.dao.UrlMonitorDao;
import de.benjaminborbe.index.dao.UrlMonitorDaoImpl;
import de.benjaminborbe.index.service.BookmarkService;
import de.benjaminborbe.index.service.BookmarkServiceImpl;
import de.benjaminborbe.index.service.TwentyFeetPerformanceService;
import de.benjaminborbe.index.service.TwentyFeetPerformanceServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UrlMonitorDao.class).to(UrlMonitorDaoImpl.class).in(Singleton.class);
		bind(TwentyFeetPerformanceService.class).to(TwentyFeetPerformanceServiceImpl.class).in(Singleton.class);
		bind(BookmarkDao.class).to(BookmarkDaoImpl.class).in(Singleton.class);
		bind(BookmarkService.class).to(BookmarkServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
