package de.benjaminborbe.httpdownloader.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.httpdownloader.gui.guice.HttpdownloaderGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpdownloaderGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new HttpdownloaderGuiModulesMock());
		final HttpdownloaderGuiServlet a = injector.getInstance(HttpdownloaderGuiServlet.class);
		final HttpdownloaderGuiServlet b = injector.getInstance(HttpdownloaderGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
