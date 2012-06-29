package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class RootNodeIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final RootNode a = injector.getInstance(RootNode.class);
		final RootNode b = injector.getInstance(RootNode.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
