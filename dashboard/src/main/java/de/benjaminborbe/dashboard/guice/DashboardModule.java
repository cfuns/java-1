package de.benjaminborbe.dashboard.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.CssResourceRenderer;
import de.benjaminborbe.dashboard.api.CssResourceRendererImpl;
import de.benjaminborbe.dashboard.api.JavascriptResourceRenderer;
import de.benjaminborbe.dashboard.api.JavascriptResourceRendererImpl;
import de.benjaminborbe.dashboard.service.DashboardWidgetRegistry;
import de.benjaminborbe.dashboard.service.DashboardWidgetRegistryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class DashboardModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JavascriptResourceRenderer.class).to(JavascriptResourceRendererImpl.class).in(Singleton.class);
		bind(CssResourceRenderer.class).to(CssResourceRendererImpl.class).in(Singleton.class);
		bind(DashboardWidgetRegistry.class).to(DashboardWidgetRegistryImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
