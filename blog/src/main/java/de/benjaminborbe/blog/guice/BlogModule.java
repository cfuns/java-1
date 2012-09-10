package de.benjaminborbe.blog.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.post.BlogPostDao;
import de.benjaminborbe.blog.post.BlogPostDaoStorage;
import de.benjaminborbe.blog.service.BlogServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class BlogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BlogPostDao.class).to(BlogPostDaoStorage.class).in(Singleton.class);
		bind(BlogService.class).to(BlogServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
