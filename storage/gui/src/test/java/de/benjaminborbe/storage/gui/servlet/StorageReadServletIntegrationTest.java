package de.benjaminborbe.storage.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.storage.gui.guice.StorageGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class StorageReadServletIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageGuiModulesMock());
		final StorageReadServlet storageReadServlet = injector.getInstance(StorageReadServlet.class);
		assertNotNull(storageReadServlet);
	}

}
