package de.benjaminborbe.dns.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.dns.gui.guice.DnsGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DnsGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DnsGuiModulesMock());
		final DnsGuiServlet a = injector.getInstance(DnsGuiServlet.class);
		final DnsGuiServlet b = injector.getInstance(DnsGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
