package de.benjaminborbe.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.BookmarkDao;
import de.benjaminborbe.index.dao.BookmarkDaoImpl;
import de.benjaminborbe.index.service.BookmarkService;
import de.benjaminborbe.index.service.BookmarkServiceImpl;
import de.benjaminborbe.index.util.IdGenerator;
import de.benjaminborbe.index.util.IdGeneratorImpl;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BookmarkDao.class).to(BookmarkDaoImpl.class).in(Singleton.class);
		bind(BookmarkService.class).to(BookmarkServiceImpl.class).in(Singleton.class);
		bind(IdGenerator.class).to(IdGeneratorImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
