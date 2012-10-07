package de.benjaminborbe.portfolio.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.portfolio.gui.guice.PortfolioGuiModulesMock;
import de.benjaminborbe.portfolio.gui.servlet.PortfolioGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PortfolioGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiServlet a = injector.getInstance(PortfolioGuiServlet.class);
		final PortfolioGuiServlet b = injector.getInstance(PortfolioGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
