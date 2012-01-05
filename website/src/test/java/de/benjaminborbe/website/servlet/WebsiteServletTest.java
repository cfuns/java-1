package de.benjaminborbe.website.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.website.guice.WebsiteModulesMock;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WebsiteServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsiteModulesMock());
		final WebsiteServlet a = injector.getInstance(WebsiteServlet.class);
		final WebsiteServlet b = injector.getInstance(WebsiteServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void service() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final WebsiteServlet websiteServlet = new WebsiteServlet(logger);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		final StringWriter sw = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(sw);
		EasyMock.expect(response.getWriter()).andReturn(printWriter);
		EasyMock.replay(response);
		websiteServlet.service(request, response);
		final String content = sw.getBuffer().toString();
		assertNotNull(content);
		assertTrue(content.indexOf("<h2>Website</h2>") != -1);
	}
}
