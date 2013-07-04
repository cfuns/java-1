package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JavascriptWidgetUnitTest {

	@Test
	public void testRender() throws Exception {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final StringWriter sw = new StringWriter();
		final PrintWriter writer = new PrintWriter(sw);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(writer).anyTimes();
		EasyMock.replay(response);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final JavascriptWidget link = new JavascriptWidget("alert('Hello World!');");
		link.render(request, response, context);
		assertEquals("<script language=\"javascript\" type=\"text/javascript\">\nalert('Hello World!');</script>\n", sw.toString());
	}
}
