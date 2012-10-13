package de.benjaminborbe.vnc.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.vnc.gui.guice.VncGuiModulesMock;
import de.benjaminborbe.vnc.gui.servlet.VncGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class VncGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncGuiModulesMock());
		final VncGuiServlet a = injector.getInstance(VncGuiServlet.class);
		final VncGuiServlet b = injector.getInstance(VncGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
