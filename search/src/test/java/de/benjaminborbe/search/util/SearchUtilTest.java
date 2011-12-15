package de.benjaminborbe.search.util;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;

public class SearchUtilTest extends TestCase {

	public void testBookmarkServiceImpl() {
		final SearchUtil bookmarkService = new SearchUtil();
		assertEquals(StringUtils.join(Arrays.asList("a").toArray(), ","),
				StringUtils.join(bookmarkService.buildSearchParts("a"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b").toArray(), ","),
				StringUtils.join(bookmarkService.buildSearchParts("a b"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b", "c").toArray(), ","),
				StringUtils.join(bookmarkService.buildSearchParts("a b - c"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b", "c", "hello").toArray(), ","),
				StringUtils.join(bookmarkService.buildSearchParts("a b - c  Hello"), ","));
	}
}
