package de.benjaminborbe.portfolio.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryCollectionComparator;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryCollectionComparatorName;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryCollectionComparatorPrio;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryEntryComparator;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryEntryComparatorName;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryEntryComparatorPrio;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiLinkFactory;
import de.benjaminborbe.portfolio.gui.widget.PortfolioLayoutWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.guice.ProviderAdapter;
import de.benjaminborbe.tools.mock.EnumerationEmpty;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PortfolioGuiGalleryServletUnitTest {

	@Test
	public void testShared() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Writer stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);

		final HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(response.getWriter()).andReturn(printWriter).anyTimes();
		EasyMock.replay(response);

		final String sessionId = "324908234890";
		final HttpSession session = EasyMock.createMock(HttpSession.class);
		EasyMock.expect(session.getId()).andReturn(sessionId).anyTimes();
		EasyMock.replay(session);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getServerPort()).andReturn(80).anyTimes();
		EasyMock.expect(request.getContextPath()).andReturn("/path").anyTimes();
		EasyMock.expect(request.getSession()).andReturn(session).anyTimes();
		EasyMock.expect(request.getScheme()).andReturn("http").anyTimes();
		EasyMock.expect(request.getServerName()).andReturn("localhost").anyTimes();
		EasyMock.expect(request.getRequestURI()).andReturn("/search").anyTimes();
		EasyMock.expect(request.getParameterNames()).andReturn(new EnumerationEmpty<String>()).anyTimes();
		EasyMock.expect(request.getParameter("gallery_id")).andReturn(null);
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
		EasyMock.expect(authenticationService.isLoggedIn(EasyMock.anyObject(SessionIdentifier.class))).andReturn(false).anyTimes();
		EasyMock.expect(authenticationService.createSessionIdentifier(request)).andReturn(sessionIdentifier).anyTimes();
		EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(userIdentifier).anyTimes();
		EasyMock.replay(authenticationService);

		final RedirectUtil redirectUtil = EasyMock.createMock(RedirectUtil.class);
		EasyMock.replay(redirectUtil);

		final String id = "1337";
		final UrlUtil urlUtil = EasyMock.createMock(UrlUtil.class);
		EasyMock.expect(urlUtil.parseId(request, PortfolioGuiConstants.PARAMETER_GALLERY_ID)).andReturn(id);
		EasyMock.replay(urlUtil);

		final PortfolioLayoutWidget portfolioLayoutWidget = EasyMock.createNiceMock(PortfolioLayoutWidget.class);
		EasyMock.replay(portfolioLayoutWidget);

		final GalleryCollectionIdentifier galleryCollectionIdentifier = new GalleryCollectionIdentifier(id);
		final GalleryCollection collection = EasyMock.createMock(GalleryCollection.class);
		EasyMock.expect(collection.getName()).andReturn("Portfolio").anyTimes();
		EasyMock.expect(collection.getId()).andReturn(galleryCollectionIdentifier).anyTimes();
		EasyMock.replay(collection);

		final GalleryService galleryService = EasyMock.createMock(GalleryService.class);
		EasyMock.expect(galleryService.createCollectionIdentifier(id)).andReturn(galleryCollectionIdentifier);
		EasyMock.expect(galleryService.getCollectionShared(galleryCollectionIdentifier)).andReturn(collection);
		EasyMock.expect(galleryService.getEntriesShared(galleryCollectionIdentifier)).andReturn(new ArrayList<GalleryEntry>());
		EasyMock.replay(galleryService);

		final PortfolioGuiLinkFactory portfolioLinkFactory = EasyMock.createNiceMock(PortfolioGuiLinkFactory.class);
		EasyMock.replay(portfolioLinkFactory);

		final PortfolioGuiGalleryCollectionComparator galleryComparator = new PortfolioGuiGalleryCollectionComparator(new PortfolioGuiGalleryCollectionComparatorName(),
			new PortfolioGuiGalleryCollectionComparatorPrio());
		final PortfolioGuiGalleryEntryComparator portfolioGuiGalleryEntryComparator = new PortfolioGuiGalleryEntryComparator(new PortfolioGuiGalleryEntryComparatorName(),
			new PortfolioGuiGalleryEntryComparatorPrio());

		final AuthorizationService authorizationService = EasyMock.createMock(AuthorizationService.class);
		authorizationService.expectAdminRole(sessionIdentifier);
		EasyMock.expect(authorizationService.hasAdminRole(sessionIdentifier)).andReturn(true);
		EasyMock.replay(authorizationService);

		final PortfolioGuiGalleryServlet servlet = new PortfolioGuiGalleryServlet(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService,
			new ProviderAdapter<>(portfolioLayoutWidget), galleryService, portfolioLinkFactory, galleryComparator, portfolioGuiGalleryEntryComparator,
			authorizationService);

		servlet.service(request, response);
		EasyMock.verify(response);
	}
}
