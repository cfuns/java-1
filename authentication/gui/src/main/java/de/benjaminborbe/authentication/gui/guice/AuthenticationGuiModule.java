package de.benjaminborbe.authentication.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfig;
import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class AuthenticationGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthenticationGuiConfig.class).to(AuthenticationGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
