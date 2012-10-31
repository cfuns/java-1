package de.benjaminborbe.tools.url;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import de.benjaminborbe.tools.map.MapChain;

public class UrlUtilImplUnitTest {

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

	@Test
	public void testBuildUrl() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("path", urlUtil.buildUrl("path", new HashMap<String, String>()));
		assertEquals("/path", urlUtil.buildUrl("/path", new HashMap<String, String>()));
		assertEquals("/path?a=b", urlUtil.buildUrl("/path", new MapChain<String, String>().add("a", "b")));
		assertEquals("/path?a=b&b=c", urlUtil.buildUrl("/path", new MapChain<String, String>().add("a", "b").add("b", "c")));
	}

	@Test
	public void testBuildUrlNull() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("/path", urlUtil.buildUrl("/path", new MapChain<String, String>().add("a", null)));
		assertEquals("/path", urlUtil.buildUrl("/path", new MapChain<String, String>().add(null, "b")));
		assertEquals("/path?b=c", urlUtil.buildUrl("/path", new MapChain<String, String>().add("a", null).add("b", "c")));
		assertEquals("/path?b=c", urlUtil.buildUrl("/path", new MapChain<String, String>().add(null, "b").add("b", "c")));
	}

	@Test
	public void testremoveTailingSlash() {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("", urlUtil.removeTailingSlash(""));
		assertEquals("/foo", urlUtil.removeTailingSlash("/foo"));
		assertEquals("/", urlUtil.removeTailingSlash("/"));
		assertEquals("/foo", urlUtil.removeTailingSlash("/foo"));
		assertEquals("/foo", urlUtil.removeTailingSlash("/foo/"));
		assertEquals("/foo", urlUtil.removeTailingSlash("/foo//"));
	}
}
