package de.benjaminborbe.scala.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.scala.api.ScalaService;
import de.benjaminborbe.scala.service.ScalaServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ScalaModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ScalaService.class).to(ScalaServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
