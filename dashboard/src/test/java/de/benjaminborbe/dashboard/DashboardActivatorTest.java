package de.benjaminborbe.dashboard;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.dashboard.DashboardActivator;
import de.benjaminborbe.dashboard.guice.DashboardModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DashboardActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DashboardModulesMock());
		final DashboardActivator o = injector.getInstance(DashboardActivator.class);
		assertNotNull(o);
	}

}
