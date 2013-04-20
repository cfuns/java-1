package de.benjaminborbe.navigation.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.navigation.api.NavigationService;
import de.benjaminborbe.navigation.service.NavigationServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class NavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NavigationService.class).to(NavigationServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
