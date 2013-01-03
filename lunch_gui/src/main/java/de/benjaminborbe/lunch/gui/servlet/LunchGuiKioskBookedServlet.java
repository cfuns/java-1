package de.benjaminborbe.lunch.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

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
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.api.LunchUser;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.lunch.gui.util.LunchUserComparator;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

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
			final LunchService lunchService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.lunchService = lunchService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
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
			}
			catch (final ParseException e) {
				calendar = calendarUtil.today(timeZoneUtil.getUTCTimeZone());
			}
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " - " + calendarUtil.toDateString(calendar)));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<LunchUser> list = new ArrayList<LunchUser>(lunchService.getBookedUser(sessionIdentifier, calendar));

			if (list.isEmpty()) {
				widgets.add("no bookings found");
			}
			else {
				Collections.sort(list, new LunchUserComparator());
				final UlWidget ul = new UlWidget();
				int counter = 0;
				for (final LunchUser user : list) {
					counter++;
					ul.add(counter + ". " + user.getName());
				}
				widgets.add(ul);
			}

			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final LunchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(LunchGuiConstants.LUNCH_ADMIN_ROLENAME);
			authorizationService.expectRole(sessionIdentifier, roleIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final AuthorizationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}
}
