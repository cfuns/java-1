package de.benjaminborbe.filestorage.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.filestorage.gui.guice.FilestorageGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class FilestorageGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageGuiModulesMock());
		final FilestorageGuiServlet a = injector.getInstance(FilestorageGuiServlet.class);
		final FilestorageGuiServlet b = injector.getInstance(FilestorageGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
