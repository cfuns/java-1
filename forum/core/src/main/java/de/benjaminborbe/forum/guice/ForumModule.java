package de.benjaminborbe.forum.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.forum.api.ForumService;
import de.benjaminborbe.forum.service.ForumServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class ForumModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ForumService.class).to(ForumServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
