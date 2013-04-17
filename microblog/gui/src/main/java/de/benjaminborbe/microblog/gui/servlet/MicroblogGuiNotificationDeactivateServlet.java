package de.benjaminborbe.microblog.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class MicroblogGuiNotificationDeactivateServlet extends WebsiteServlet {

	private static final long serialVersionUID = -7862318070826148848L;

	private final MicroblogService microblogService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public MicroblogGuiNotificationDeactivateServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final MicroblogService microblogService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.microblogService = microblogService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final String keyword = request.getParameter(MicroblogGuiConstants.PARAEMTER_NOTIFICATION_KEYWORD);
			microblogService.deactivateNotification(userIdentifier, keyword);
		} catch (final AuthenticationServiceException | ValidationException | MicroblogServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
