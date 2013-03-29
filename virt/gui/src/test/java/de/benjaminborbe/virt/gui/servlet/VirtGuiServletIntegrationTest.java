package de.benjaminborbe.virt.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.virt.gui.guice.VirtGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class VirtGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtGuiModulesMock());
		final VirtGuiServlet a = injector.getInstance(VirtGuiServlet.class);
		final VirtGuiServlet b = injector.getInstance(VirtGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
