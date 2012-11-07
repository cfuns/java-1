package de.benjaminborbe.website.form;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;

public class FormSelectboxWidgetUnitTest {

	@Test
	public void testGetName() throws Exception {
		final String name = "test123";
		final FormSelectboxWidget field = new FormSelectboxWidget(name);
		assertEquals(name, field.getName());

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

		field.render(request, response, context);

		assertEquals("<select name=\"" + name + "\"></select><br/>", stringWriter.toString());
	}

	@Test
	public void testOption() throws Exception {
		final String name = "test123";
		final FormSelectboxWidget field = new FormSelectboxWidget(name);
		assertEquals(name, field.getName());

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

		field.addOption("value1", "name1");

		field.render(request, response, context);

		assertEquals("<select name=\"" + name + "\"><option value=\"value1\">name1</option></select><br/>", stringWriter.toString());
	}

	@Test
	public void testOptionSelected() throws Exception {
		final String name = "test123";
		final FormSelectboxWidget field = new FormSelectboxWidget(name);
		assertEquals(name, field.getName());

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

		field.addOption("value1", "name1");
		field.addOption("value2", "name2");
		field.addDefaultValue("value2");

		field.render(request, response, context);

		assertEquals("<select name=\"" + name + "\"><option value=\"value1\">name1</option><option selected=\"selected\" value=\"value2\">name2</option></select><br/>",
				stringWriter.toString());
	}

	@Test
	public void testLabel() throws Exception {
		final String name = "test123";
		final FormSelectboxWidget field = new FormSelectboxWidget(name);

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

		final String label = "label123";
		field.addLabel(label);

		field.render(request, response, context);

		assertEquals("<label for=\"" + name + "\">" + label + "</label><select name=\"" + name + "\"></select><br/>", stringWriter.toString());
	}
}
