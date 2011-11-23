package de.benjaminborbe.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.BookmarkDao;
import de.benjaminborbe.index.dao.BookmarkDaoImpl;
import de.benjaminborbe.index.service.BookmarkService;
import de.benjaminborbe.index.service.BookmarkServiceImpl;
import de.benjaminborbe.index.service.TwentyFeetPerformanceService;
import de.benjaminborbe.index.service.TwentyFeetPerformanceServiceImpl;
import de.benjaminborbe.index.util.HttpDownloader;
import de.benjaminborbe.index.util.HttpDownloaderImpl;
import de.benjaminborbe.index.util.IdGenerator;
import de.benjaminborbe.index.util.IdGeneratorImpl;
import de.benjaminborbe.index.util.ThreadRunner;
import de.benjaminborbe.index.util.ThreadRunnerImpl;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ThreadRunner.class).to(ThreadRunnerImpl.class).in(Singleton.class);
		bind(HttpDownloader.class).to(HttpDownloaderImpl.class).in(Singleton.class);
		bind(TwentyFeetPerformanceService.class).to(TwentyFeetPerformanceServiceImpl.class).in(Singleton.class);
		bind(BookmarkDao.class).to(BookmarkDaoImpl.class).in(Singleton.class);
		bind(BookmarkService.class).to(BookmarkServiceImpl.class).in(Singleton.class);
		bind(IdGenerator.class).to(IdGeneratorImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
