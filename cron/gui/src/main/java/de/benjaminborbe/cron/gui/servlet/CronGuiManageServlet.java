package de.benjaminborbe.cron.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronControllerException;
import de.benjaminborbe.cron.gui.CronGuiConstants;
import de.benjaminborbe.cron.gui.util.CronGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CronGuiManageServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Cron";

	private final CronController cronController;

	private final Logger logger;

	private final CronGuiLinkFactory cronGuiLinkFactory;

	@Inject
	public CronGuiManageServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final CronController cronController,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final CronGuiLinkFactory cronGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.cronController = cronController;
		this.cronGuiLinkFactory = cronGuiLinkFactory;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("service");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String action = request.getParameter(CronGuiConstants.PARAMETER_ACTION);
			if (CronGuiConstants.ACTION_START.equals(action)) {
				cronController.start();
				widgets.add("started");
				widgets.add(new BrWidget());
			} else if (CronGuiConstants.ACTION_STOP.equals(action)) {
				cronController.stop();
				widgets.add("stopped");
				widgets.add(new BrWidget());
			}
			widgets.add("cron is " + (cronController.isRunning() ? "" : "not ") + "running");
			widgets.add(new BrWidget());
			widgets.add(cronGuiLinkFactory.cronStart(request));
			widgets.add(new BrWidget());
			widgets.add(cronGuiLinkFactory.cronStop(request));
			widgets.add(new BrWidget());

			return widgets;
		} catch (final CronControllerException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
