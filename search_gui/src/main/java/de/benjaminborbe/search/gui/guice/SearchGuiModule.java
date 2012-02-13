package de.benjaminborbe.search.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchFactory;
import de.benjaminborbe.search.gui.service.SearchGuiSpecialSearchFactoryImpl;
import de.benjaminborbe.search.gui.service.SearchGuiWidgetImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.website.util.CssResourceRendererImpl;
import de.benjaminborbe.website.util.JavascriptResourceRendererImpl;

public class SearchGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SearchGuiSpecialSearchFactory.class).to(SearchGuiSpecialSearchFactoryImpl.class).in(Singleton.class);
		bind(SearchWidget.class).to(SearchGuiWidgetImpl.class).in(Singleton.class);
		bind(JavascriptResourceRenderer.class).to(JavascriptResourceRendererImpl.class).in(Singleton.class);
		bind(CssResourceRenderer.class).to(CssResourceRendererImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
