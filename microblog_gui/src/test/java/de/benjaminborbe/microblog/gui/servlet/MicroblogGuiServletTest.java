package de.benjaminborbe.microblog.gui.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;
import com.google.inject.Provider;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.gui.guice.MicroblogGuiModulesMock;
import de.benjaminborbe.microblog.gui.servlet.MicroblogGuiServlet;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ParseUtil;

public class MicroblogGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiServlet a = injector.getInstance(MicroblogGuiServlet.class);
		final MicroblogGuiServlet b = injector.getInstance(MicroblogGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void service() throws Exception {

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		final StringWriter sw = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(sw);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final Collection<CssResource> cssResources = new HashSet<CssResource>();

		final CssResourceRenderer cssResourceRenderer = EasyMock.createMock(CssResourceRenderer.class);
		cssResourceRenderer.render(request, response, cssResources);
		EasyMock.replay(cssResourceRenderer);

		final Collection<JavascriptResource> javascriptResources = new HashSet<JavascriptResource>();

		final JavascriptResourceRenderer javascriptResourceRenderer = EasyMock.createMock(JavascriptResourceRenderer.class);
		javascriptResourceRenderer.render(request, response, javascriptResources);
		EasyMock.replay(javascriptResourceRenderer);

		final TimeZone timeZone = EasyMock.createMock(TimeZone.class);
		EasyMock.replay(timeZone);

		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.expect(timeZoneUtil.getUTCTimeZone()).andReturn(timeZone).anyTimes();
		EasyMock.replay(timeZoneUtil);

		final long startTime = 42l;
		final long endTime = 1337l;

		final Calendar calendar = EasyMock.createMock(Calendar.class);
		EasyMock.expect(calendar.getTimeInMillis()).andReturn(startTime);
		EasyMock.expect(calendar.getTimeInMillis()).andReturn(endTime);
		EasyMock.replay(calendar);

		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		EasyMock.expect(calendarUtil.now(timeZone)).andReturn(calendar).anyTimes();
		EasyMock.replay(calendarUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseLong(String.valueOf(startTime), endTime)).andReturn(startTime);
		EasyMock.replay(parseUtil);

		final Map<String, String> data = new HashMap<String, String>();

		final HttpContext httpContext = EasyMock.createMock(HttpContext.class);
		EasyMock.expect(httpContext.getData()).andReturn(data).anyTimes();
		EasyMock.replay(httpContext);

		final NavigationWidget navigationWidget = EasyMock.createMock(NavigationWidget.class);
		navigationWidget.render(request, response, httpContext);
		EasyMock.replay(navigationWidget);

		final Provider<HttpContext> httpContextProvider = new Provider<HttpContext>() {

			@Override
			public HttpContext get() {
				return httpContext;
			}
		};
		final MicroblogService microblogRevisionStorage = EasyMock.createMock(MicroblogService.class);
		EasyMock.expect(microblogRevisionStorage.getLastRevision()).andReturn(null);
		EasyMock.replay(microblogRevisionStorage);

		final MicroblogGuiServlet microblogServlet = new MicroblogGuiServlet(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget,
				httpContextProvider, microblogRevisionStorage);

		microblogServlet.service(request, response);
		final String content = sw.getBuffer().toString();
		assertNotNull(content);
		assertTrue(content.indexOf("<h1>Microblog</h1>") != -1);
	}
}