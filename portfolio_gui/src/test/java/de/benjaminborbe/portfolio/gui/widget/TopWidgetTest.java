package de.benjaminborbe.portfolio.gui.widget;

import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class TopWidgetTest {

	@Test
	public void testRender() throws Exception {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.expect(request.getScheme()).andReturn("http");
		EasyMock.expect(request.getServerName()).andReturn("www.test.de");
		EasyMock.replay(request);

		final StringWriter sw = new StringWriter();
		final PrintWriter writer = new PrintWriter(sw);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(writer).anyTimes();
		EasyMock.replay(response);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final TopWidget widget = new TopWidget(null);
		widget.render(request, response, context);
		assertNotNull(sw.toString());
	}
}
