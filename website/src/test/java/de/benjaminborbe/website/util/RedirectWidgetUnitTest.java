package de.benjaminborbe.website.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class RedirectWidgetUnitTest {

	@Test
	public void testRedirect() throws Exception {
		final String url = "http://www.google.com";
		final RedirectWidget widget = new RedirectWidget(url);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(url);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		widget.render(request, response, context);

		EasyMock.verify(request, response, context);
	}
}
