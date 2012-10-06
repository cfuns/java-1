package de.benjaminborbe.storage.gui.servlet;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.gui.guice.StorageGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageReadServletIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageGuiModulesMock());
		final StorageReadServlet storageReadServlet = injector.getInstance(StorageReadServlet.class);
		assertNotNull(storageReadServlet);
	}

}
