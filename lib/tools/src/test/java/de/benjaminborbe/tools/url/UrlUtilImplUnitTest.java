package de.benjaminborbe.tools.url;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

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
		assertEquals("path", urlUtil.buildUrl("path", new MapParameter()));
		assertEquals("/path", urlUtil.buildUrl("/path", new MapParameter()));
		assertEquals("/path?a=b", urlUtil.buildUrl("/path", new MapParameter().add("a", "b")));
		assertEquals("/path?a=b&b=c", urlUtil.buildUrl("/path", new MapParameter().add("a", "b").add("b", "c")));
	}

	@Test
	public void testBuildUrlNull() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		assertEquals("/path", urlUtil.buildUrl("/path", new MapParameter().add("a", new String[] { null })));
		assertEquals("/path", urlUtil.buildUrl("/path", new MapParameter().add(null, "b")));
		assertEquals("/path?b=c", urlUtil.buildUrl("/path", new MapParameter().add("a", new String[] { null }).add("b", "c")));
		assertEquals("/path?b=c", urlUtil.buildUrl("/path", new MapParameter().add(null, "b").add("b", "c")));
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

	@Test
	public void testParseImageId() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		final String parameterName = "foo";
		{
			final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
			EasyMock.expect(request.getParameter(parameterName)).andReturn("123");
			EasyMock.replay(request);
			assertEquals("123", urlUtil.parseId(request, parameterName));
		}
		{
			final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
			EasyMock.expect(request.getParameter(parameterName)).andReturn(null);
			EasyMock.expect(request.getRequestURI()).andReturn("/bla/content/123");
			EasyMock.replay(request);
			assertEquals("123", urlUtil.parseId(request, parameterName));
		}
		{
			final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
			EasyMock.expect(request.getParameter(parameterName)).andReturn(null);
			EasyMock.expect(request.getRequestURI()).andReturn("/bla/content/123.jpg");
			EasyMock.replay(request);
			assertEquals("123", urlUtil.parseId(request, parameterName));
		}
	}
}
