package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class SingleTagWidgetUnitTest {

	@Test
	public void testGetTag() throws Exception {
		final String tag = "input";
		final SingleTagWidget singleTagWidget = new SingleTagWidget(tag);
		assertEquals(tag, singleTagWidget.getTag());
	}

	@Test
	public void testRender() throws Exception {
		final String name = "input";
		final SingleTagWidget singleTagWidget = new SingleTagWidget(name);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		singleTagWidget.render(request, response, context);

		assertEquals("<input/>", stringWriter.toString());
	}

	@Test
	public void testRenderWithAttribute() throws Exception {
		final String name = "input";
		final SingleTagWidget singleTagWidget = new SingleTagWidget(name);
		singleTagWidget.addAttribute("type", "submit");

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		singleTagWidget.render(request, response, context);

		assertEquals("<input type=\"submit\"/>", stringWriter.toString());
	}
}
