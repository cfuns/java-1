package de.benjaminborbe.monitoring.check;

import com.google.inject.Injector;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MonitoringCheckRegistryIntegrationTest {

	@Test
	public void testTypes() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final MonitoringCheckRegistry factory = injector.getInstance(MonitoringCheckRegistry.class);
		assertNotNull(factory);
		final MonitoringCheckRegistry monitoringCheckRegistry = injector.getInstance(MonitoringCheckRegistry.class);

		for (final MonitoringCheck type : monitoringCheckRegistry.getAll()) {
			final MonitoringCheck check = factory.get(type.getId());
			assertNotNull("no check found for type: " + type, check);
			assertNotNull("requireParameters is null for type: " + type, check.getRequireParameters());
		}
	}
}
