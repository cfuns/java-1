package de.benjaminborbe.monitoring.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringSummaryWidget;
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiSummaryWidgetImpl;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiWidgetImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MonitoringSummaryWidget.class).to(MonitoringGuiSummaryWidgetImpl.class).in(Singleton.class);
		bind(MonitoringWidget.class).to(MonitoringGuiWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
