package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Provider;
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
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public abstract class PokerGuiPlayerJsonServlet extends PokerGuiJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PokerService pokerService;

	@Inject
	public PokerGuiPlayerJsonServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final PokerService pokerService,
		final PokerGuiConfig pokerGuiConfig
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider, pokerGuiConfig);
		this.pokerService = pokerService;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
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
		} catch (final PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
