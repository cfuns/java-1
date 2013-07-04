package de.benjaminborbe.mail.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.mail.gui.guice.MailGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MailGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MailGuiModulesMock());
		final MailGuiServlet a = injector.getInstance(MailGuiServlet.class);
		final MailGuiServlet b = injector.getInstance(MailGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
