package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.HttpContext;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FormInputTextareaWidgetUnitTest {

	@Test
	public void testGetName() {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);
		assertEquals(name, formInputTextareaWidget.getName());
	}

	@Test
	public void testRender() throws Exception {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(null).anyTimes();
		EasyMock.replay(request);

		formInputTextareaWidget.render(request, response, context);

		assertEquals("<textarea name=\"" + name + "\"></textarea><br/>", stringWriter.toString());
	}

	@Test
	public void testAddLabel() {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);
		final String label = "testLabel";
		assertNotNull(formInputTextareaWidget.addLabel(label));
	}

	@Test
	public void testAddDefaultValue() {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);
		final String defaultValue = "defaultValue";
		assertNotNull(formInputTextareaWidget.addDefaultValue(defaultValue));
	}

	@Test
	public void testAddPlaceholder() {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);
		final String placeholder = "placeholder";
		assertNotNull(formInputTextareaWidget.addPlaceholder(placeholder));
	}

	@Test
	public void testAddId() {
		final String name = "test123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);
		final String id = "id";
		assertNotNull(formInputTextareaWidget.addId(id));
	}

	@Test
	public void testValue() throws Exception {
		final String name = "test123";
		final String value = "value123";
		final FormInputTextareaWidget formInputTextareaWidget = new FormInputTextareaWidget(name);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(name)).andReturn(value).anyTimes();
		EasyMock.replay(request);

		formInputTextareaWidget.render(request, response, context);

		assertEquals("<textarea name=\"" + name + "\">" + value + "</textarea><br/>", stringWriter.toString());
	}
}
