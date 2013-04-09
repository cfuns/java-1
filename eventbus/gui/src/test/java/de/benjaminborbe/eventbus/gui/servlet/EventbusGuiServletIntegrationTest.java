package de.benjaminborbe.eventbus.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.gui.guice.EventbusGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusGuiModulesMock());
		final EventbusGuiServlet a = injector.getInstance(EventbusGuiServlet.class);
		final EventbusGuiServlet b = injector.getInstance(EventbusGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
