package de.benjaminborbe.wiki.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.wiki.gui.converter.WikiGuiMarkupConverter;
import de.benjaminborbe.wiki.gui.converter.WikiGuiMarkupConverterImpl;

public class WikiGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(WikiGuiMarkupConverter.class).to(WikiGuiMarkupConverterImpl.class).in(Singleton.class);
	}
}
