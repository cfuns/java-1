package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.IdGeneratorLong;

public class BookmarkDaoTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkDao a = injector.getInstance(BookmarkDao.class);
		final BookmarkDao b = injector.getInstance(BookmarkDao.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testIdGeneratorLong() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final IdGeneratorLong idGeneratorLong = injector.getInstance(IdGeneratorLong.class);
		assertNotNull(idGeneratorLong);
	}
}
