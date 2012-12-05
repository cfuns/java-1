package de.benjaminborbe.website.form;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class FormCheckboxWidgetUnitTest {

	@Test
	public void testNotChecked() throws Exception {
		final String name = "bla";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn("").anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.render(request, response, context);

		assertEquals("<input name=\"" + name + "\" type=\"checkbox\" value=\"true\"/>", stringWriter.toString());
	}

	@Test
	public void testChecked() throws Exception {
		final String name = "bla";
		final String value = "true";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"true\"/>", stringWriter.toString());
	}

	@Test
	public void testDefaultChecked() throws Exception {
		final String name = "bla";
		final String value = "true";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(null).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.addDefaultValue(value);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"true\"/>", stringWriter.toString());
	}

	@Test
	public void testValue() throws Exception {
		final String name = "bla";
		final String value = "true";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn("false").anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.addValue(value);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"true\"/>", stringWriter.toString());
	}
}
