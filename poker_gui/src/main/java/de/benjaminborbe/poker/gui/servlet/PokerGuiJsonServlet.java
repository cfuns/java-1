package de.benjaminborbe.poker.gui.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

@Singleton
public abstract class PokerGuiJsonServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PokerService pokerService;

	private final PokerGuiConfig pokerGuiConfig;

	@Inject
	public PokerGuiJsonServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final PokerService pokerService,
			final PokerGuiConfig pokerGuiConfig) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.pokerService = pokerService;
		this.pokerGuiConfig = pokerGuiConfig;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final String token = request.getParameter(PokerGuiConstants.PARAMETER_TOKEN);
			final PokerPlayerIdentifier playerIdentifier = pokerService.createPlayerIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_PLAYER_ID));
			final PokerPlayer player = pokerService.getPlayer(playerIdentifier);
			if (player == null) {
				throw new PermissionDeniedException("invalid player");
			}
			if (token == null || !player.getToken().equals(token)) {
				throw new PermissionDeniedException("invalid token");
			}
		}
		catch (final PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return pokerGuiConfig.isJsonApiEnabled();
	}

	protected abstract void doAction(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws PokerServiceException,
			ValidationException, ServletException, IOException, PermissionDeniedException, LoginRequiredException;

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			doAction(request, response, context);
		}
		catch (final ValidationException e) {
			final Collection<ValidationError> errors = e.getErrors();
			final StringBuffer sb = new StringBuffer();
			for (final ValidationError error : errors) {
				sb.append(error.getMessage());
				sb.append(" ");
			}
			printError(response, sb.toString());
		}
		catch (final PokerServiceException e) {
			printException(response, e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}
}
