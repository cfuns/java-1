package de.benjaminborbe.index.dao;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexGuiceInjectorBuilderMock;
import junit.framework.TestCase;

public class BookmarkDaoTest extends TestCase {

	public void testSingleton() {
		final Injector injector = IndexGuiceInjectorBuilderMock.getInjector();
		final BookmarkDao a = injector.getInstance(BookmarkDao.class);
		final BookmarkDao b = injector.getInstance(BookmarkDao.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
