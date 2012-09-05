package de.benjaminborbe.blog.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.blog.gui.guice.BlogGuiModulesMock;
import de.benjaminborbe.blog.gui.servlet.BlogGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BlogGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BlogGuiModulesMock());
		final BlogGuiServlet a = injector.getInstance(BlogGuiServlet.class);
		final BlogGuiServlet b = injector.getInstance(BlogGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
