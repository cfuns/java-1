package de.benjaminborbe.monitoring.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.gui.service.MonitoringGuiSummaryWidgetImpl;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiWidgetCache;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiWidgetCacheImpl;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiWidgetLive;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiWidgetLiveImpl;
import de.benjaminborbe.monitoring.gui.service.MonitoringSummaryWidget;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MonitoringGuiWidgetLive.class).to(MonitoringGuiWidgetLiveImpl.class).in(Singleton.class);
		bind(MonitoringGuiWidgetCache.class).to(MonitoringGuiWidgetCacheImpl.class).in(Singleton.class);

		bind(MonitoringSummaryWidget.class).to(MonitoringGuiSummaryWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
