package de.benjaminborbe.gallery.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.gui.util.GalleryGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.guice.ProviderAdapter;
import de.benjaminborbe.tools.mock.EnumerationEmpty;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.StringWidget;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GalleryGuiCollectionListServletUnitTest {

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
		final String groupId = "123";
		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId).anyTimes();
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getServerPort()).andReturn(80).anyTimes();
		EasyMock.expect(request.getContextPath()).andReturn("/path").anyTimes();
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		EasyMock.expect(request.getScheme()).andReturn("http").anyTimes();
		EasyMock.expect(request.getServerName()).andReturn("localhost").anyTimes();
		EasyMock.expect(request.getRequestURI()).andReturn("/path").anyTimes();
		EasyMock.expect(request.getParameterNames()).andReturn(new EnumerationEmpty<String>()).anyTimes();
		EasyMock.expect(request.getParameter("group_id")).andReturn(groupId).anyTimes();
		EasyMock.replay(request);

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

		final Map<String, String> data = new HashMap<>();

		final HttpContext httpContext = EasyMock.createMock(HttpContext.class);
		EasyMock.expect(httpContext.getData()).andReturn(data).anyTimes();
		EasyMock.replay(httpContext);

		final NavigationWidget navigationWidget = EasyMock.createMock(NavigationWidget.class);
		navigationWidget.render(request, response, httpContext);
		EasyMock.replay(navigationWidget);

		final Provider<HttpContext> httpContextProvider = new ProviderAdapter<>(httpContext);

		final SessionIdentifier sessionIdentifier = EasyMock.createMock(SessionIdentifier.class);
		EasyMock.replay(sessionIdentifier);

		final UserIdentifier userIdentifier = EasyMock.createMock(UserIdentifier.class);
		EasyMock.replay(userIdentifier);

		final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
		EasyMock.expect(authenticationService.createSessionIdentifier(request)).andReturn(sessionIdentifier).anyTimes();
		EasyMock.expect(authenticationService.isLoggedIn(EasyMock.anyObject(SessionIdentifier.class))).andReturn(true).anyTimes();
		EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(userIdentifier).anyTimes();
		EasyMock.replay(authenticationService);

		final RedirectUtil redirectUtil = EasyMock.createMock(RedirectUtil.class);
		EasyMock.replay(redirectUtil);

		final UrlUtil urlUtil = EasyMock.createMock(UrlUtil.class);
		EasyMock.replay(urlUtil);

		final GalleryGroup group = EasyMock.createMock(GalleryGroup.class);
		EasyMock.expect(group.getName()).andReturn("foo");
		EasyMock.replay(group);

		final GalleryGroupIdentifier galleryGroupIdentifier = new GalleryGroupIdentifier(groupId);
		final GalleryService galleryService = EasyMock.createMock(GalleryService.class);
		EasyMock.expect(galleryService.getCollectionsWithGroup(sessionIdentifier, galleryGroupIdentifier)).andReturn(new HashSet<GalleryCollection>()).anyTimes();
		EasyMock.expect(galleryService.createGroupIdentifier(groupId)).andReturn(galleryGroupIdentifier).anyTimes();
		EasyMock.expect(galleryService.getGroup(sessionIdentifier, galleryGroupIdentifier)).andReturn(group);
		galleryService.expectPermission(sessionIdentifier);
		EasyMock.replay(galleryService);

		final GalleryGuiLinkFactory linkFactory = EasyMock.createNiceMock(GalleryGuiLinkFactory.class);
		EasyMock.expect(linkFactory.createCollection(request, galleryGroupIdentifier)).andReturn(new StringWidget(""));
		EasyMock.replay(linkFactory);

		final AuthorizationService authorizationService = EasyMock.createMock(AuthorizationService.class);
		EasyMock.replay(authorizationService);

		final CacheService cacheService = EasyMock.createMock(CacheService.class);
		EasyMock.expect(cacheService.get("hostname")).andReturn("localhost").anyTimes();
		EasyMock.replay(cacheService);

		final GalleryGuiCollectionListServlet galleryServlet = new GalleryGuiCollectionListServlet(logger, calendarUtil, timeZoneUtil, parseUtil, authenticationService,
			navigationWidget, httpContextProvider, urlUtil, linkFactory, galleryService, authorizationService, null, cacheService);

		galleryServlet.service(request, response);
		final String content = sw.getBuffer().toString();
		assertNotNull(content);
		assertTrue(content.contains("<h1>foo - Collections</h1>"));
	}
}
