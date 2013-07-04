package de.benjaminborbe.googlesearch.gui.service;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class GooglesearchGuiSpecialSearchUnitTest {

	@Test
	public void testGoogleSearch() throws Exception {
		testRedirect("g: ben", "http://www.google.de/search?sourceid=bb&ie=UTF-8&q=ben");
		testRedirect("g: hello world", "http://www.google.de/search?sourceid=bb&ie=UTF-8&q=hello+world");
	}

	protected void testRedirect(final String searchTerm, final String redirectLocation) throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final UrlUtil urlUtil = new UrlUtilImpl();
		final Widget widget = new GooglesearchGuiSpecialSearch(urlUtil);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter("q")).andReturn(searchTerm);
		EasyMock.replay(request);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(redirectLocation);
		EasyMock.replay(response);

		final HttpContext context = EasyMock.createMock(HttpContext.class);

		EasyMock.replay(context);

		widget.render(request, response, context);

		EasyMock.verify(response);
	}

	@Test
	public void testBuildRedirect() throws Exception {
		final UrlUtil urlUtil = new UrlUtilImpl();
		final GooglesearchGuiSpecialSearch googlesearchGuiSpecialSearch = new GooglesearchGuiSpecialSearch(urlUtil);
		assertEquals("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=foo", googlesearchGuiSpecialSearch.buildRedirect("g: foo"));
		assertEquals("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=foo", googlesearchGuiSpecialSearch.buildRedirect("g:  foo"));
		assertEquals("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=foo", googlesearchGuiSpecialSearch.buildRedirect("g: foo "));
		assertEquals("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=foo", googlesearchGuiSpecialSearch.buildRedirect("g:foo"));
	}
}
