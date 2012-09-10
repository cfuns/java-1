package de.benjaminborbe.blog.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.blog.gui.guice.BlogGuiModulesMock;
import de.benjaminborbe.blog.gui.servlet.BlogGuiLatestPostsServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BlogGuiLatestPostsServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BlogGuiModulesMock());
		final BlogGuiLatestPostsServlet a = injector.getInstance(BlogGuiLatestPostsServlet.class);
		final BlogGuiLatestPostsServlet b = injector.getInstance(BlogGuiLatestPostsServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
