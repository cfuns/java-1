package de.benjaminborbe.blog.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.blog.gui.guice.BlogGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
