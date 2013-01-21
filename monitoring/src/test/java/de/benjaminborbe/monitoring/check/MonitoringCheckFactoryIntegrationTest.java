package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MonitoringCheckFactoryIntegrationTest {

	@Test
	public void testTypes() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final MonitoringCheckFactory factory = injector.getInstance(MonitoringCheckFactory.class);
		assertNotNull(factory);
		for (final MonitoringCheckType type : MonitoringCheckType.values()) {
			final MonitoringCheck check = factory.get(type);
			assertNotNull("no check found for type: " + type, check);
			assertNotNull("requireParameters is null for type: " + type, check.getRequireParameters());
		}
	}
}
