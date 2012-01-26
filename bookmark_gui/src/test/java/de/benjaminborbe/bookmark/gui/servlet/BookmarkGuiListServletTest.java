package de.benjaminborbe.bookmark.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.gui.guice.BookmarkGuiModulesMock;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiListServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkGuiListServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkGuiModulesMock());
		final BookmarkGuiListServlet a = injector.getInstance(BookmarkGuiListServlet.class);
		final BookmarkGuiListServlet b = injector.getInstance(BookmarkGuiListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
