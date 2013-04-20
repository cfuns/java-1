package de.benjaminborbe.util.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

@Singleton
public class UtilGuiDayDiffServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3897185107545429460L;

	private static final String TITLE = "Util - Day-Diff";

	private static final String FROM_DATE = "from";

	private static final String UNTIL_DATE = "until";

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public UtilGuiDayDiffServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		try {
			final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
			final Calendar fromCalendar = calendarUtil.parseDate(timeZone, request.getParameter(FROM_DATE));
			final Calendar untilCalendar = calendarUtil.parseDate(timeZone, request.getParameter(UNTIL_DATE));

			final long diff = Math.abs(fromCalendar.getTimeInMillis() - untilCalendar.getTimeInMillis());
			final long days = diff / 24 / 3600 / 1000;

			widgets.add("difference in days = " + days);
		} catch (final ParseException e) {
			widgets.add("invalid input");
		}

		return widgets;
	}

}
