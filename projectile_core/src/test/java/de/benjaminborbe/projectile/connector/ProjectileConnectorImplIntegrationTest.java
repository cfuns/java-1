package de.benjaminborbe.projectile.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.projectile.guice.ProjectileModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ProjectileConnectorImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileModulesMock());
		final ProjectileConnector connector = injector.getInstance(ProjectileConnector.class);
		assertNotNull(connector);
		assertEquals(ProjectileConnectorImpl.class, connector.getClass());
	}
}
