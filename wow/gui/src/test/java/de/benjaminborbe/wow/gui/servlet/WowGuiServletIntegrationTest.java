package de.benjaminborbe.wow.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.wow.gui.guice.WowGuiModulesMock;

public class WowGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WowGuiModulesMock());
		final WowGuiServlet a = injector.getInstance(WowGuiServlet.class);
		final WowGuiServlet b = injector.getInstance(WowGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
