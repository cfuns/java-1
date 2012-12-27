package de.benjaminborbe.shortener.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.shortener.gui.guice.ShortenerGuiModulesMock;
import de.benjaminborbe.shortener.gui.servlet.ShortenerGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ShortenerGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ShortenerGuiModulesMock());
		final ShortenerGuiServlet a = injector.getInstance(ShortenerGuiServlet.class);
		final ShortenerGuiServlet b = injector.getInstance(ShortenerGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
