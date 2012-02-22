package de.benjaminborbe.storage.servlet;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageReadServletIntegrationTest {

	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageReadServlet storageReadServlet = injector.getInstance(StorageReadServlet.class);
		assertNotNull(storageReadServlet);
	}

}
