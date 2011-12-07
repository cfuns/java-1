package de.benjaminborbe.index.dao;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkDaoTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final BookmarkDao a = injector.getInstance(BookmarkDao.class);
		final BookmarkDao b = injector.getInstance(BookmarkDao.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
