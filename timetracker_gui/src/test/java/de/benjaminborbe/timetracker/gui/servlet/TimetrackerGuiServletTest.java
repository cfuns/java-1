package de.benjaminborbe.timetracker.gui.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.timetracker.gui.guice.TimetrackerGuiModulesMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ParseUtil;

public class TimetrackerGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerGuiModulesMock());
		final TimetrackerGuiServlet a = injector.getInstance(TimetrackerGuiServlet.class);
		final TimetrackerGuiServlet b = injector.getInstance(TimetrackerGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testService() throws Exception {

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		final StringWriter sw = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(sw);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final String sessionId = "324908234890";
		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId).anyTimes();
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/path").anyTimes();
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		EasyMock.expect(request.getScheme()).andReturn("http").anyTimes();
		EasyMock.expect(request.getServerName()).andReturn("localhost").anyTimes();
		EasyMock.replay(request);

		final CssResourceRenderer cssResourceRenderer = EasyMock.createMock(CssResourceRenderer.class);
		cssResourceRenderer
				.render(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(HttpServletResponse.class), EasyMock.anyObject(HttpContext.class), EasyMock.anyObject(Collection.class));
		EasyMock.replay(cssResourceRenderer);

		final JavascriptResourceRenderer javascriptResourceRenderer = EasyMock.createMock(JavascriptResourceRenderer.class);
		javascriptResourceRenderer.render(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(HttpServletResponse.class), EasyMock.anyObject(HttpContext.class),
				EasyMock.anyObject(Collection.class));
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

		final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
		EasyMock.expect(authenticationService.isLoggedIn(EasyMock.anyObject(SessionIdentifier.class))).andReturn(false).anyTimes();
		EasyMock.expect(authenticationService);

		EasyMock.replay(authenticationService);

		final TimetrackerGuiServlet timetrackerServlet = new TimetrackerGuiServlet(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, authenticationService,
				navigationWidget, httpContextProvider);

		timetrackerServlet.service(request, response);
		final String content = sw.getBuffer().toString();
		assertNotNull(content);
		assertTrue(content.indexOf("<h1>Timetracker</h1>") != -1);
	}
}
