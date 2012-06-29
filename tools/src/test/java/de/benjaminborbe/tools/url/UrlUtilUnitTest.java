package de.benjaminborbe.tools.url;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlUtilUnitTest {

	@Test
	public void testEncode() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("ben", urlUtil.encode("ben"));
		assertEquals("hello+world", urlUtil.encode("hello world"));
	}

	@Test
	public void testDecode() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("ben", urlUtil.encode("ben"));
		assertEquals("hello world", urlUtil.decode("hello+world"));
	}
}
