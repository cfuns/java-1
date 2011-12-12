package de.benjaminborbe.bookmark.servlet;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.bookmark.servlet.BookmarkServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkServlet a = injector.getInstance(BookmarkServlet.class);
		final BookmarkServlet b = injector.getInstance(BookmarkServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
