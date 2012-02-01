package de.benjaminborbe.dashboard.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetImpl;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetRegistry;
import de.benjaminborbe.dashboard.gui.service.DashboardGuiWidgetRegistryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class DashboardGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DashboardGuiWidgetRegistry.class).to(DashboardGuiWidgetRegistryImpl.class).in(Singleton.class);
		bind(DashboardWidget.class).to(DashboardGuiWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
