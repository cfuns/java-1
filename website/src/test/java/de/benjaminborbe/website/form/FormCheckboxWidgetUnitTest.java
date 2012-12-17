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
	public void testLabel() throws Exception {
		final String name = "bla";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn("").anyTimes();
		EasyMock.expect(request.getParameterValues(name)).andReturn(new String[] { "" }).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		final String label = "Label";
		checkbox.addLabel(label);
		checkbox.render(request, response, context);

		assertEquals("<label for=\"" + name + "\">" + label + "</label><input name=\"" + name + "\" type=\"checkbox\" value=\"true\"/><br/>", stringWriter.toString());
	}

	@Test
	public void testNotChecked() throws Exception {
		final String name = "bla";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn("").anyTimes();
		EasyMock.expect(request.getParameterValues(name)).andReturn(new String[] { "" }).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.render(request, response, context);

		assertEquals("<input name=\"" + name + "\" type=\"checkbox\" value=\"true\"/><br/>", stringWriter.toString());
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
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value).anyTimes();
		EasyMock.expect(request.getParameterValues(name)).andReturn(new String[] { value }).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"true\"/><br/>", stringWriter.toString());
	}

	@Test
	public void testDefaultChecked() throws Exception {
		final String name = "bla";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(null).anyTimes();
		EasyMock.expect(request.getParameterValues(name)).andReturn(new String[0]).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.setCheckedDefault(true);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"true\"/><br/>", stringWriter.toString());
	}

	@Test
	public void testValue() throws Exception {
		final String name = "bla";
		final String value = "foo";

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value).anyTimes();
		EasyMock.expect(request.getParameterValues(name)).andReturn(new String[] { value }).anyTimes();
		EasyMock.replay(request);

		final FormCheckboxWidget checkbox = new FormCheckboxWidget(name);
		checkbox.addValue(value);
		checkbox.render(request, response, context);

		assertEquals("<input checked=\"checked\" name=\"" + name + "\" type=\"checkbox\" value=\"" + value + "\"/><br/>", stringWriter.toString());
	}
}
