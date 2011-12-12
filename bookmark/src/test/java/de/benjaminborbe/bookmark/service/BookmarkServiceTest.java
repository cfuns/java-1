package de.benjaminborbe.bookmark.service;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkServiceTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkService a = injector.getInstance(BookmarkService.class);
		final BookmarkService b = injector.getInstance(BookmarkService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
