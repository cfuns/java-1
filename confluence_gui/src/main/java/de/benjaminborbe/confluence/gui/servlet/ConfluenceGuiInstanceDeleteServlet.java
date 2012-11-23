package de.benjaminborbe.confluence.gui.servlet;

import java.io.IOException;

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
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.gui.ConfluenceGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class ConfluenceGuiInstanceDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final ConfluenceService confluenceService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public ConfluenceGuiInstanceDeleteServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final ConfluenceService confluenceService) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.confluenceService = confluenceService;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ConfluenceInstanceIdentifier confluenceInstanceIdentifier = confluenceService.createConfluenceInstanceIdentifier(sessionIdentifier,
					request.getParameter(ConfluenceGuiConstants.PARAMETER_INSTANCE_ID));
			confluenceService.deleteConfluenceInstance(sessionIdentifier, confluenceInstanceIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final ConfluenceServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final LoginRequiredException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final PermissionDeniedException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final SuperAdminRequiredException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

}
