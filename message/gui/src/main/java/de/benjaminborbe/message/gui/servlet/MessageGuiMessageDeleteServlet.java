package de.benjaminborbe.message.gui.servlet;

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
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.message.gui.MessageGuiConstants;
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
public class MessageGuiMessageDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final MessageService messageService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public MessageGuiMessageDeleteServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final MessageService messageService,
		final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.messageService = messageService;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
		LoginRequiredException, PermissionDeniedException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final MessageIdentifier messageIdentifier = messageService.createMessageIdentifier(request.getParameter(MessageGuiConstants.PARAMETER_MESSAGE_ID));
			messageService.deleteById(sessionIdentifier, messageIdentifier);
		} catch (final AuthenticationServiceException | MessageServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

}
