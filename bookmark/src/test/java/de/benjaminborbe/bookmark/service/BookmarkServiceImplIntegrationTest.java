package de.benjaminborbe.bookmark.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkServiceImplIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkService a = injector.getInstance(BookmarkService.class);
		final BookmarkService b = injector.getInstance(BookmarkService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test(expected = PermissionDeniedException.class)
	public void testDescription() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkService bookmarkService = injector.getInstance(BookmarkService.class);

		final String sessionId = "asdf";
		final SessionIdentifier sessionIdentifier = new SessionIdentifier(sessionId);

		bookmarkService.getBookmarks(sessionIdentifier);
	}

}
