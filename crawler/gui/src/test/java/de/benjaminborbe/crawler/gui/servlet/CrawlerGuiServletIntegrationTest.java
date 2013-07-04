package de.benjaminborbe.crawler.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.crawler.gui.guice.CrawlerGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CrawlerGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CrawlerGuiModulesMock());
		final CrawlerGuiServlet a = injector.getInstance(CrawlerGuiServlet.class);
		final CrawlerGuiServlet b = injector.getInstance(CrawlerGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
