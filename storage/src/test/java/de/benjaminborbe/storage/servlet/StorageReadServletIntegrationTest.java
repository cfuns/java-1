package de.benjaminborbe.storage.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.servlet.StorageReadServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageReadServletIntegrationTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageReadServlet o = injector.getInstance(StorageReadServlet.class);
		assertNotNull(o);
	}

}
