package de.benjaminborbe.website.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.website.util.CssResourceRendererImpl;
import de.benjaminborbe.website.util.JavascriptResourceRendererImpl;

public class WebsiteModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CssResourceRenderer.class).to(CssResourceRendererImpl.class).in(Singleton.class);
		bind(JavascriptResourceRenderer.class).to(JavascriptResourceRendererImpl.class).in(Singleton.class);
	}

}
