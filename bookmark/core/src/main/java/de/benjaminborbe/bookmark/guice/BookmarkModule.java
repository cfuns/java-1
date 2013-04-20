package de.benjaminborbe.bookmark.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.dao.BookmarkDao;
import de.benjaminborbe.bookmark.dao.BookmarkDaoStorage;
import de.benjaminborbe.bookmark.service.BookmarkServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class BookmarkModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BookmarkDao.class).to(BookmarkDaoStorage.class).in(Singleton.class);
		bind(BookmarkService.class).to(BookmarkServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(BookmarkValidatorLinker.class);
	}
}
