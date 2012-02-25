package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.IdGeneratorLong;
import de.benjaminborbe.tools.util.ThreadResult;

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

		final ThreadResult<Long> counter = new ThreadResult<Long>();
		counter.set(1337l);
		final BookmarkIdGenerator bookmarkIdGenerator = EasyMock.createMock(BookmarkIdGenerator.class);
		final IAnswer<BookmarkIdentifier> answer = new IAnswer<BookmarkIdentifier>() {

			@Override
			public BookmarkIdentifier answer() throws Throwable {
				counter.set(counter.get() + 1);
				return new BookmarkIdentifier(counter.get());
			}
		};
		EasyMock.expect(bookmarkIdGenerator.nextId()).andStubAnswer(answer);
		EasyMock.replay(bookmarkIdGenerator);

		final BookmarkDao bookmarkDao = new BookmarkDaoImpl(logger, bookmarkIdGenerator, bookmarkBeanProvider);
		final Collection<BookmarkBean> bookmarks = bookmarkDao.getFavorites(userIdentifier);
		assertTrue(bookmarks.size() > 0);
		for (final BookmarkBean bookmark : bookmarks) {
			assertTrue(bookmark.isFavorite());
		}
	}
}
