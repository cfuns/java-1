package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.IdGeneratorLong;
import de.benjaminborbe.tools.util.IdGeneratorLongImpl;

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

	@Test
	public void testGetFavorites() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final IdGeneratorLong idGeneratorLong = new IdGeneratorLongImpl();

		final Provider<BookmarkBean> bookmarkBeanProvider = new Provider<BookmarkBean>() {

			@Override
			public BookmarkBean get() {
				return new BookmarkBean();
			}
		};

		final String username = "bborbe";
		final UserIdentifier userIdentifier = EasyMock.createMock(UserIdentifier.class);
		EasyMock.expect(userIdentifier.getId()).andReturn(username).anyTimes();
		EasyMock.replay(userIdentifier);

		final BookmarkDao bookmarkDao = new BookmarkDaoImpl(logger, idGeneratorLong, bookmarkBeanProvider);
		final Collection<BookmarkBean> bookmarks = bookmarkDao.getFavorites(userIdentifier);
		assertTrue(bookmarks.size() > 0);
		for (final BookmarkBean bookmark : bookmarks) {
			assertTrue(bookmark.isFavorite());
		}
	}
}
