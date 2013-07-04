package de.benjaminborbe.monitoring.service;

import com.google.inject.Injector;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MonitoringCheckCronJobIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		assertNotNull(injector.getInstance(MonitoringCheckCronJob.class));
	}
}
