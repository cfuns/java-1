package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;
import java.text.DecimalFormat;

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
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class ProjectileGuiSlacktimeServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private static final String TITLE = "Projectile - Slacktime";

	private final ProjectileService projectileService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public ProjectileGuiSlacktimeServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final ProjectileService projectileService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.projectileService = projectileService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
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
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ProjectileSlacktimeReport report = projectileService.getSlacktimeReport(sessionIdentifier);

			widgets.add(createBlock("Week", report.getWeekIntern(), report.getWeekExtern()));
			widgets.add(createBlock("Month", report.getMonthIntern(), report.getMonthExtern()));
			widgets.add(createBlock("Year", report.getYearIntern(), report.getYearExtern()));

			return widgets;
		}
		catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private Widget createBlock(final String name, final Double intern, final Double extern) {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget(name));
		if (extern != null && intern != null) {
			final double total = extern + intern;
			final double procent = (extern / total) * 100;
			final DecimalFormat df = new DecimalFormat("#####0.0h");
			widgets.add(df.format(procent) + "%");
			widgets.add(" ");
			widgets.add("(");
			widgets.add("extern: " + df.format(extern));
			widgets.add(" ");
			widgets.add("intern: " + df.format(intern));
			widgets.add(")");
		}
		else {
			widgets.add("-");
		}
		return widgets;
	}
}
