package de.benjaminborbe.monitoring;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final MonitoringActivator o = injector.getInstance(MonitoringActivator.class);
		assertNotNull(o);
	}
}
