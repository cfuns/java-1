package de.benjaminborbe.authentication.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfig;
import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AuthenticationGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthenticationGuiConfig.class).to(AuthenticationGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
