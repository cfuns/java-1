package de.benjaminborbe.messageservice.gui.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.inject.Injector;
import de.benjaminborbe.messageservice.gui.guice.MessageserviceGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MessageserviceGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MessageserviceGuiModulesMock());
		final MessageserviceGuiServlet a = injector.getInstance(MessageserviceGuiServlet.class);
		final MessageserviceGuiServlet b = injector.getInstance(MessageserviceGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
