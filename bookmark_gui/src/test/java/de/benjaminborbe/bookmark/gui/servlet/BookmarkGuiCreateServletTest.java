package de.benjaminborbe.bookmark.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.gui.guice.BookmarkGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkGuiCreateServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkGuiModulesMock());
		final BookmarkGuiCreateServlet a = injector.getInstance(BookmarkGuiCreateServlet.class);
		final BookmarkGuiCreateServlet b = injector.getInstance(BookmarkGuiCreateServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
