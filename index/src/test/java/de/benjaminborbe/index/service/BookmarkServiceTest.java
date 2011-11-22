package de.benjaminborbe.index.service;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexGuiceInjectorBuilderMock;

public class BookmarkServiceTest extends TestCase {

	public void testSingleton() {
		final Injector injector = IndexGuiceInjectorBuilderMock.getInjector();
		final BookmarkService a = injector.getInstance(BookmarkService.class);
		final BookmarkService b = injector.getInstance(BookmarkService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
