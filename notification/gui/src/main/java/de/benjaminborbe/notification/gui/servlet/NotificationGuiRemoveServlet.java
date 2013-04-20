package de.benjaminborbe.notification.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.gui.NotificationGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class NotificationGuiRemoveServlet extends WebsiteServlet {

	private static final long serialVersionUID = -7862318070826148848L;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final NotificationService notificationService;

	@Inject
	public NotificationGuiRemoveServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final NotificationService notificationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.notificationService = notificationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final NotificationMediaIdentifier notificationMediaIdentifier = notificationService.createNotificationMediaIdentifier(request
					.getParameter(NotificationGuiConstants.PARAMETER_MEDIA));
			final NotificationTypeIdentifier notificationTypeIdentifier = notificationService.createNotificationTypeIdentifier(request
					.getParameter(NotificationGuiConstants.PARAMETER_TYPE));
			logger.info("deactive media " + notificationMediaIdentifier + " for type " + notificationTypeIdentifier);
			notificationService.remove(sessionIdentifier, notificationMediaIdentifier, notificationTypeIdentifier);
		}
		catch (final AuthenticationServiceException | NotificationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
