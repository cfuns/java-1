package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkBeanTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkBean bookmarkBean = injector.getInstance(BookmarkBean.class);
		assertNotNull(bookmarkBean);
	}

	@Test
	public void testId() {
		final Long id = 1337l;
		final BookmarkBean bookmarkBean = new BookmarkBean();
		assertNull(bookmarkBean.getId());
		bookmarkBean.setId(new BookmarkIdentifier(id));
		assertEquals(String.valueOf(id), bookmarkBean.getId().getId());
	}
}
