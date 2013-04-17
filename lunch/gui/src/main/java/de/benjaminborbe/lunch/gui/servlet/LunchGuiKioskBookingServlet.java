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
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormElementWidget;
import de.benjaminborbe.website.form.FormElementWidgetDummy;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class LunchGuiKioskBookingServlet extends LunchGuiHtmlServlet {

	private static final long serialVersionUID = -7698261881382004351L;

	private static final String TITLE = "Lunch - Kiosk - Booking";

	private final Logger logger;

	private final LunchService lunchService;

	private final CalendarUtil calendarUtil;

	private final AuthenticationService authenticationService;

	private final TimeZoneUtil timeZoneUtil;

	private final LunchGuiLinkFactory lunchGuiLinkFactory;

	private final AuthorizationService authorizationService;

	private final ParseUtil parseUtil;

	@Inject
	public LunchGuiKioskBookingServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final LunchGuiLinkFactory lunchGuiLinkFactory,
		final LunchService lunchService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.lunchGuiLinkFactory = lunchGuiLinkFactory;
		this.lunchService = lunchService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");

			Calendar calendar;
			try {
				calendar = calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), request.getParameter(LunchGuiConstants.PARAMETER_BOOKING_DATE));
			} catch (final ParseException e) {
				calendar = calendarUtil.today(timeZoneUtil.getUTCTimeZone());
			}

			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " - " + calendarUtil.toDateString(calendar)));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String[] selectedUsers = request.getParameterValues(LunchGuiConstants.PARAMETER_BOOKING_USER);
			if (selectedUsers != null && selectedUsers.length > 0) {
				logger.info("book user: " + StringUtils.join(selectedUsers, ","));
				lunchService.book(sessionIdentifier, calendar, build(selectedUsers));
				widgets.add("booking completed");
				return widgets;
			}

			{
				final ListWidget links = new ListWidget();
				links.add(lunchGuiLinkFactory.bookingSubDay(request, calendar));
				links.add(" ");
				links.add(lunchGuiLinkFactory.bookingToday(request));
				links.add(" ");
				links.add(lunchGuiLinkFactory.bookingAddDay(request, calendar));
				links.add(" ");
				widgets.add(new DivWidget(links));
			}

			final List<KioskUser> users = new ArrayList<>(lunchService.getSubscribeUser(calendar));
			Collections.sort(users, new KioskUserComparator());

			final FormWidget form = new FormWidget().addId("bookings");

			int counter = 0;
			for (final KioskUser user : users) {
				counter++;
				if (user.getCustomerNumber() != null) {
					final FormElementWidget input = new FormCheckboxWidget(LunchGuiConstants.PARAMETER_BOOKING_USER).addLabel(counter + ". " + user.getName())
						.addValue(user.getCustomerNumber()).setCheckedDefault(true);
					form.addFormInputWidget(input);
				} else {
					form.addFormInputWidget(new FormElementWidgetDummy().addLabel(counter + ". " + user.getName()).addContent("NOT FOUND"));
				}
			}
			form.addFormInputWidget(new FormInputHiddenWidget(LunchGuiConstants.PARAMETER_BOOKING_DATE).addValue(calendarUtil.toDateString(calendar)));
			form.addFormInputWidget(new FormInputSubmitWidget("buchen"));
			widgets.add(form);
			widgets.add(new LinkWidget("javascript:toggle()", "toggle"));

			widgets.add(new BrWidget());
			{
				final ListWidget links = new ListWidget();
				links.add(lunchGuiLinkFactory.bookedForDay(request, calendar));
				links.add(" ");
				widgets.add(links);
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
		} catch (final ParseException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private Collection<Long> build(final String[] users) throws ParseException {
		final List<Long> result = new ArrayList<>();
		for (final String user : users) {
			result.add(parseUtil.parseLong(user));
		}
		return result;
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
