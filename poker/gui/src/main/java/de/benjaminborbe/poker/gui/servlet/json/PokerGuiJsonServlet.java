package de.benjaminborbe.poker.gui.servlet.json;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Singleton
public abstract class PokerGuiJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final PokerGuiConfig pokerGuiConfig;

	private final PokerService pokerService;

	@Inject
	public PokerGuiJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final PokerGuiConfig pokerGuiConfig, final PokerService pokerService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.pokerGuiConfig = pokerGuiConfig;
		this.pokerService = pokerService;
	}

	@Override
	public boolean isEnabled() {
		final boolean jsonApiEnabled = pokerService.isJsonApiEnabled();
		logger.debug("jsonApiEnabled: " + jsonApiEnabled);
		return jsonApiEnabled;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	protected abstract void doAction(final HttpServletRequest request, final HttpServletResponse response) throws PokerServiceException,
		ValidationException, ServletException, IOException, PermissionDeniedException, LoginRequiredException;

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			doAction(request, response);
		} catch (final ValidationException e) {
			final Collection<ValidationError> errors = e.getErrors();
			final StringBuffer sb = new StringBuffer();
			for (final ValidationError error : errors) {
				sb.append(error.getMessage());
				sb.append(" ");
			}
			printError(response, sb.toString());
		} catch (final PokerServiceException e) {
			printException(response, e);
		}
	}

}
