package de.benjaminborbe.selenium.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.selenium.gui.guice.SeleniumGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SeleniumGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumGuiModulesMock());
		final SeleniumGuiServlet a = injector.getInstance(SeleniumGuiServlet.class);
		final SeleniumGuiServlet b = injector.getInstance(SeleniumGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
