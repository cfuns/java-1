package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.url.UrlUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PokerGuiActionFoldJsonServlet extends PokerGuiPlayerJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PokerService pokerService;

	@Inject
	public PokerGuiActionFoldJsonServlet(
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
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider, pokerService, pokerGuiConfig);
		this.pokerService = pokerService;
	}

	@Override
	protected void doAction(final HttpServletRequest request, final HttpServletResponse response) throws PokerServiceException, ValidationException,
		ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		final PokerPlayerIdentifier playerIdentifier = pokerService.createPlayerIdentifier(request.getParameter(PokerGuiConstants.PARAMETER_PLAYER_ID));
		final PokerPlayer player = pokerService.getPlayer(playerIdentifier);
		if (player.getGame() == null) {
			throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("player has no game")));
		}

		pokerService.fold(player.getGame(), playerIdentifier);

		final JSONObject jsonObject = new JSONObjectSimple();
		jsonObject.put("success", "true");
		printJson(response, jsonObject);
	}
}
