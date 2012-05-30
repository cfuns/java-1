package de.benjaminborbe.monitoring.gui.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringGuiDashboardWidgetTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiDashboardWidget monitoringGuiDashboardWidget = injector.getInstance(MonitoringGuiDashboardWidget.class);
		assertNotNull(monitoringGuiDashboardWidget);
	}
}
