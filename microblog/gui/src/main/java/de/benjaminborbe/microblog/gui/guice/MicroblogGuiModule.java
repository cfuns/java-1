package de.benjaminborbe.microblog.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.microblog.gui.config.MicroblogGuiConfig;
import de.benjaminborbe.microblog.gui.config.MicroblogGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class MicroblogGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MicroblogGuiConfig.class).to(MicroblogGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
