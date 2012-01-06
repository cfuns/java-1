package de.benjaminborbe.navigation.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.service.NavigationWidgetImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class NavigationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NavigationWidget.class).to(NavigationWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
