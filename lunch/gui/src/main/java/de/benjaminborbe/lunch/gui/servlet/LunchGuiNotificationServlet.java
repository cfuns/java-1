package de.benjaminborbe.lunch.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.lunch.gui.util.LunchGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class LunchGuiNotificationServlet extends LunchGuiHtmlServlet {

	private static final long serialVersionUID = -7698261881382004351L;

	private static final String TITLE = "Lunch - Notification";

	private final Logger logger;

	private final LunchService lunchService;

	private final AuthenticationService authenticationService;

	private final LunchGuiLinkFactory lunchGuiLinkFactory;

	@Inject
	public LunchGuiNotificationServlet(
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
		this.authenticationService = authenticationService;
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
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final String action = request.getParameter(LunchGuiConstants.PARAMETER_ACTION);
			if (LunchGuiConstants.ACTION_ACTIVATE.equals(action)) {
				lunchService.activateNotification(userIdentifier);
			}
			if (LunchGuiConstants.ACTION_DEACTIVATE.equals(action)) {
				lunchService.deactivateNotification(userIdentifier);
			}

			if (lunchService.isNotificationActivated(userIdentifier)) {
				widgets.add("notification active");
				widgets.add(new BrWidget());
				widgets.add(lunchGuiLinkFactory.deactivateNotification(request));
			}
			else {
				widgets.add("notification inactive");
				widgets.add(new BrWidget());
				widgets.add(lunchGuiLinkFactory.activateNotification(request));
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
		catch (final ValidationException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
