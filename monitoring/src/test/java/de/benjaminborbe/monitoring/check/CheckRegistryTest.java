package de.benjaminborbe.monitoring.check;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CheckRegistryTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final CheckRegistry o1 = injector.getInstance(CheckRegistry.class);
		final CheckRegistry o2 = injector.getInstance(CheckRegistry.class);
		assertTrue(o1 == o2);
	}

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final CheckRegistry o = injector.getInstance(CheckRegistry.class);
		assertNotNull(o);
	}

	public void testNotEmpty() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final CheckRegistry o = injector.getInstance(CheckRegistry.class);
		assertNotNull(o);
		assertTrue(o.getAll().size() > 1);
	}
}
