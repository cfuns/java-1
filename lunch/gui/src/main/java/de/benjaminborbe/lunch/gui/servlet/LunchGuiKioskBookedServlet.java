package de.benjaminborbe.lunch.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.kiosk.api.KioskUser;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.lunch.gui.util.KioskUserComparator;
import de.benjaminborbe.lunch.gui.util.LunchGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Singleton
public class LunchGuiKioskBookedServlet extends LunchGuiHtmlServlet {

	private static final long serialVersionUID = -7698261881382004351L;

	private static final String TITLE = "Lunch - Kiosk - Booked";

	private final Logger logger;

	private final LunchService lunchService;

	private final CalendarUtil calendarUtil;

	private final AuthenticationService authenticationService;

	private final TimeZoneUtil timeZoneUtil;

	private final AuthorizationService authorizationService;

	private final LunchGuiLinkFactory lunchGuiLinkFactory;

	@Inject
	public LunchGuiKioskBookedServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final LunchService lunchService,
		final LunchGuiLinkFactory lunchGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.lunchService = lunchService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.lunchGuiLinkFactory = lunchGuiLinkFactory;

	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			Calendar calendar;
			try {
				calendar = calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), request.getParameter(LunchGuiConstants.PARAMETER_BOOKED_DATE));
			} catch (final ParseException e) {
				calendar = calendarUtil.today(timeZoneUtil.getUTCTimeZone());
			}
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " - " + calendarUtil.toDateString(calendar)));

			{
				final ListWidget links = new ListWidget();
				links.add(lunchGuiLinkFactory.bookedSubDay(request, calendar));
				links.add(" ");
				links.add(lunchGuiLinkFactory.bookedToday(request));
				links.add(" ");
				links.add(lunchGuiLinkFactory.bookedAddDay(request, calendar));
				links.add(" ");
				widgets.add(new DivWidget(links));
			}

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<KioskUser> list = new ArrayList<>(lunchService.getBookedUser(calendar));

			if (list.isEmpty()) {
				widgets.add("no bookings found");
			} else {
				Collections.sort(list, new KioskUserComparator());
				final UlWidget ul = new UlWidget();
				int counter = 0;
				for (final KioskUser user : list) {
					counter++;
					ul.add(counter + ". " + user.getName());
				}
				widgets.add(ul);
			}

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final LunchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(LunchService.PERMISSION_BOOKING);
			authorizationService.expectPermission(sessionIdentifier, roleIdentifier);
		} catch (final AuthenticationServiceException | AuthorizationServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}
}
