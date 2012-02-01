package de.benjaminborbe.search.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class SearchGuiUtilTest {

	@Test
	public void BookmarkServiceImpl() {
		final SearchGuiUtil bookmarkService = new SearchGuiUtil();
		assertEquals(StringUtils.join(Arrays.asList("a").toArray(), ","), StringUtils.join(bookmarkService.buildSearchParts("a"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b").toArray(), ","), StringUtils.join(bookmarkService.buildSearchParts("a b"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b", "c").toArray(), ","), StringUtils.join(bookmarkService.buildSearchParts("a b - c"), ","));
		assertEquals(StringUtils.join(Arrays.asList("a", "b", "c", "hello").toArray(), ","), StringUtils.join(bookmarkService.buildSearchParts("a b - c  Hello"), ","));
		assertEquals(StringUtils.join(Arrays.asList("ec2").toArray(), ","), StringUtils.join(bookmarkService.buildSearchParts("ec2"), ","));
	}
}
