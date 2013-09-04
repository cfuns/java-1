package de.benjaminborbe.projectile.service;

import com.google.inject.Injector;
import de.benjaminborbe.projectile.guice.ProjectileModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ProjectileMonitoringCheckRemoteIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileModulesMock());
		assertNotNull(injector.getInstance(ProjectileMonitoringCheckRemote.class));
	}

}
