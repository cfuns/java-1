package de.benjaminborbe.notification.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.notification.gui.guice.NotificationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NotificationGuiSendServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NotificationGuiModulesMock());
		final NotificationGuiSendServlet a = injector.getInstance(NotificationGuiSendServlet.class);
		final NotificationGuiSendServlet b = injector.getInstance(NotificationGuiSendServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
