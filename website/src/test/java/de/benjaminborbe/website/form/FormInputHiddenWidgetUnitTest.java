package de.benjaminborbe.website.form;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputHiddenWidgetUnitTest {

	@Test
	public void testGetName() {
		final String value = "test123";
		final FormInputHiddenWidget formInputTextareaWidget = new FormInputHiddenWidget(value);
		assertEquals(value, formInputTextareaWidget.getName());
	}

	@Test
	public void testRenderValue() throws Exception, IOException {
		final String name = "fieldName";
		final String value = "fieldValue";
		final FormInputHiddenWidget formInputTextareaWidget = new FormInputHiddenWidget(name);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value);
		EasyMock.replay(request);

		formInputTextareaWidget.render(request, response, context);

		assertEquals("<input name=\"" + name + "\" type=\"hidden\" value=\"" + value + "\"/>", stringWriter.toString());
	}

	@Test
	public void testRender() throws Exception, IOException {
		final String name = "fieldName";
		final FormInputHiddenWidget formInputTextareaWidget = new FormInputHiddenWidget(name);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(null);
		EasyMock.replay(request);

		formInputTextareaWidget.render(request, response, context);

		assertEquals("<input name=\"" + name + "\" type=\"hidden\" value=\"\"/>", stringWriter.toString());
	}
}
