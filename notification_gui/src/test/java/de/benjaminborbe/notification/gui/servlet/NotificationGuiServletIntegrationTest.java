package de.benjaminborbe.notification.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.notification.gui.guice.NotificationGuiModulesMock;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NotificationGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NotificationGuiModulesMock());
		final NotificationGuiServlet a = injector.getInstance(NotificationGuiServlet.class);
		final NotificationGuiServlet b = injector.getInstance(NotificationGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
