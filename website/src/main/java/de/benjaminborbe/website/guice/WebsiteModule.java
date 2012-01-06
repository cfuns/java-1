package de.benjaminborbe.website.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.CssResourceRendererImpl;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRendererImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class WebsiteModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JavascriptResourceRenderer.class).to(JavascriptResourceRendererImpl.class).in(Singleton.class);
		bind(CssResourceRenderer.class).to(CssResourceRendererImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
