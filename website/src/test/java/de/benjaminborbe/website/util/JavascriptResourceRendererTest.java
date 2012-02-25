package de.benjaminborbe.website.util;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;

public class JavascriptResourceRendererTest {

	@Test
	public void testNotResource() throws Exception {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final StringWriter sw = new StringWriter();
		final PrintWriter writer = new PrintWriter(sw);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(writer).anyTimes();
		EasyMock.replay(response);

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final Set<JavascriptResource> resources = new HashSet<JavascriptResource>();
		final JavascriptResourceWidget link = new JavascriptResourceWidget(resources);
		link.render(request, response, context);
		assertEquals("", sw.toString());
	}

	@Test
	public void testResource() throws Exception {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final StringWriter sw = new StringWriter();
		final PrintWriter writer = new PrintWriter(sw);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(writer).anyTimes();
		EasyMock.replay(response);

		final List<JavascriptResource> resources = new ArrayList<JavascriptResource>();
		{
			final JavascriptResource resource = EasyMock.createMock(JavascriptResource.class);
			EasyMock.expect(resource.getUrl()).andReturn("http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js");
			EasyMock.replay(resource);
			resources.add(resource);
		}
		{
			final JavascriptResource resource = EasyMock.createMock(JavascriptResource.class);
			EasyMock.expect(resource.getUrl()).andReturn("http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js");
			EasyMock.replay(resource);
			resources.add(resource);
		}

		final HttpContext context = EasyMock.createMock(HttpContext.class);
		EasyMock.replay(context);

		final JavascriptResourceWidget link = new JavascriptResourceWidget(resources);
		link.render(request, response, context);
		assertEquals(
				"<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js\"></script>\n<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js\"></script>\n",
				sw.toString());
	}
}
