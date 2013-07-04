package de.benjaminborbe.slash.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.slash.gui.util.SlashGuiRedirectDeterminer;
import de.benjaminborbe.slash.gui.util.SlashGuiRedirectDeterminerImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SlashGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SlashGuiRedirectDeterminer.class).to(SlashGuiRedirectDeterminerImpl.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
