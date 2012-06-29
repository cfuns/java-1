package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.benjaminborbe.bookmark.api.BookmarkIdentifier;

public class BookmarkBeanUnitTest {

	@Test
	public void testId() {
		final Long id = 1337l;
		final BookmarkBean bookmarkBean = new BookmarkBean();
		assertNull(bookmarkBean.getId());
		bookmarkBean.setId(new BookmarkIdentifier(id));
		assertEquals(String.valueOf(id), bookmarkBean.getId().getId());
	}
}
