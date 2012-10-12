package de.benjaminborbe.website.form;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputBaseWidgetTest {

	@Test
	public void testRender() throws Exception {
		final String name = "username";
		final String value = null;
		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value);
		EasyMock.replay(request);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final FormInputBaseWidget form = new FormInputBaseWidget("text", name);
		form.render(request, response, context);

		assertEquals("<input name=\"" + name + "\" type=\"text\" value=\"\"/><br/>", stringWriter.toString());
	}

	@Test
	public void testRenderLabel() throws Exception {
		final String name = "username";
		final String value = null;
		final String label = "User";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value);
		EasyMock.replay(request);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final FormInputBaseWidget form = new FormInputBaseWidget("text", name).addLabel(label);
		form.render(request, response, context);

		assertEquals("<label for=\"" + name + "\">User</label><input name=\"" + name + "\" type=\"text\" value=\"\"/><br/>", stringWriter.toString());
	}
}
