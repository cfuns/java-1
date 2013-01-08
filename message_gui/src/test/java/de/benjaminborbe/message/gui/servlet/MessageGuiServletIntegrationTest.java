package de.benjaminborbe.message.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.message.gui.guice.MessageGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MessageGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MessageGuiModulesMock());
		final MessageGuiServlet a = injector.getInstance(MessageGuiServlet.class);
		final MessageGuiServlet b = injector.getInstance(MessageGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
