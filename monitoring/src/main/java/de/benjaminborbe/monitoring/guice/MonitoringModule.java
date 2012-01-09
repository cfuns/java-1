package de.benjaminborbe.monitoring.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringSummaryWidget;
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.NodeCheckerImpl;
import de.benjaminborbe.monitoring.service.MonitoringSummaryWidgetImpl;
import de.benjaminborbe.monitoring.service.MonitoringWidgetImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MonitoringSummaryWidget.class).to(MonitoringSummaryWidgetImpl.class).in(Singleton.class);
		bind(MonitoringWidget.class).to(MonitoringWidgetImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(NodeChecker.class).to(NodeCheckerImpl.class).in(Singleton.class);
	}
}
