package de.benjaminborbe.xmpp.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.xmpp.gui.guice.XmppGuiModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmppGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppGuiModulesMock());
		final XmppGuiServlet a = injector.getInstance(XmppGuiServlet.class);
		final XmppGuiServlet b = injector.getInstance(XmppGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
