package de.benjaminborbe.storage.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.gui.guice.StorageGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageGuiModulesMock());
		final StorageGuiServlet a = injector.getInstance(StorageGuiServlet.class);
		final StorageGuiServlet b = injector.getInstance(StorageGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
