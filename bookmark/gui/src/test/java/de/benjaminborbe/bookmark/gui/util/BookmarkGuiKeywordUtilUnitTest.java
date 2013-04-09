package de.benjaminborbe.bookmark.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class BookmarkGuiKeywordUtilUnitTest {

	@Test
	public void testSplit() {
		final BookmarkGuiKeywordUtil bookmarkGuiKeywordUtil = new BookmarkGuiKeywordUtil();
		final List<String> list = bookmarkGuiKeywordUtil.buildKeywords(" Contact phone  Email ");
		assertNotNull(list);
		assertEquals(3, list.size());
		assertEquals("contact", list.get(0));
		assertEquals("email", list.get(1));
		assertEquals("phone", list.get(2));
	}
}
