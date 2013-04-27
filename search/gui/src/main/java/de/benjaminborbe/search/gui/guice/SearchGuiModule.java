package de.benjaminborbe.search.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.gui.config.SearchGuiConfig;
import de.benjaminborbe.search.gui.config.SearchGuiConfigImpl;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchFactory;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchFactoryImpl;
import de.benjaminborbe.search.gui.service.SearchGuiWidgetImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SearchGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SearchGuiConfig.class).to(SearchGuiConfigImpl.class).in(Singleton.class);
		bind(SearchGuiSpecialSearchFactory.class).to(SearchGuiSpecialSearchFactoryImpl.class).in(Singleton.class);
		bind(SearchWidget.class).to(SearchGuiWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
