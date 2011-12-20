package de.benjaminborbe.eventbus.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.eventbus.servlet.EventbusServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusServlet a = injector.getInstance(EventbusServlet.class);
		final EventbusServlet b = injector.getInstance(EventbusServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
