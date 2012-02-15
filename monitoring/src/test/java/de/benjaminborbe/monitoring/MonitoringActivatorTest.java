package de.benjaminborbe.monitoring;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringActivatorTest {

	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final MonitoringActivator o = injector.getInstance(MonitoringActivator.class);
		assertNotNull(o);
	}

}
