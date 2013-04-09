package de.benjaminborbe.forum.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.forum.api.ForumService;
import de.benjaminborbe.forum.service.ForumServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ForumModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ForumService.class).to(ForumServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
