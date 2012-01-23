package de.benjaminborbe.bookmark.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkCreateServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkCreateServlet a = injector.getInstance(BookmarkCreateServlet.class);
		final BookmarkCreateServlet b = injector.getInstance(BookmarkCreateServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
