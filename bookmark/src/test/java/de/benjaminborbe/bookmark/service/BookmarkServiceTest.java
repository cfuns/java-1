package de.benjaminborbe.bookmark.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkServiceTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkService a = injector.getInstance(BookmarkService.class);
		final BookmarkService b = injector.getInstance(BookmarkService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testDescription() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkService a = injector.getInstance(BookmarkService.class);

		final String sessionId = "asdf";
		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		for (final Bookmark bookmark : a.getBookmarks(sessionIdentifier)) {
			assertNotNull(bookmark.getDescription());
			assertNotNull(bookmark.getUrl());
			assertNotNull(bookmark.getName());
		}
	}

}
