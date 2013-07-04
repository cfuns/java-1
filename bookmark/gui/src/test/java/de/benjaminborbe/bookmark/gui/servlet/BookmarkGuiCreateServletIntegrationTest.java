package de.benjaminborbe.bookmark.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.bookmark.gui.guice.BookmarkGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookmarkGuiCreateServletIntegrationTest {

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
