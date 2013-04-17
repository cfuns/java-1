package de.benjaminborbe.util.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.util.gui.UtilGuiConstants;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class UtilGuiTimeServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 6210254616531116633L;

	private static final String TITLE = "Util - Time";

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ParseUtil parseUtil;

	@Inject
	public UtilGuiTimeServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		final long time = parseUtil.parseLong(request.getParameter(UtilGuiConstants.PARAMETER_TIME), calendarUtil.getTime());
		final List<TimeZone> timezones = new ArrayList<>();
		timezones.add(timeZoneUtil.getUTCTimeZone());
		timezones.add(timeZoneUtil.getEuropeBerlinTimeZone());
		final UlWidget ul = new UlWidget();
		ul.add("unixtime: " + time);
		for (final TimeZone timeZone : timezones) {
			final Calendar calendar = calendarUtil.getCalendar(timeZone, time);
			ul.add(timeZone.getDisplayName() + ": " + calendarUtil.toDateTimeString(calendar));
		}
		widgets.add(ul);

		final FormWidget form = new FormWidget();
		form.addFormInputWidget(new FormInputTextWidget(UtilGuiConstants.PARAMETER_TIME).addLabel("Time:").addDefaultValue(time));
		form.addFormInputWidget(new FormInputSubmitWidget("calc"));
		widgets.add(form);

		return widgets;
	}
}
