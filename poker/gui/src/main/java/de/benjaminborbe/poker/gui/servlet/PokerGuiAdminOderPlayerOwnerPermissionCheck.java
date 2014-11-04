package de.benjaminborbe.poker.gui.servlet;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;

import javax.inject.Inject;

public class PokerGuiAdminOderPlayerOwnerPermissionCheck {

	private final PokerService pokerService;

	private final AuthenticationService authenticationService;

	@Inject
	public PokerGuiAdminOderPlayerOwnerPermissionCheck(final PokerService pokerService, final AuthenticationService authenticationService) {
		this.pokerService = pokerService;
		this.authenticationService = authenticationService;
	}

	public boolean hasPermission(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayer pokerPlayer
	) throws AuthenticationServiceException, LoginRequiredException, PokerServiceException {
		return pokerService.hasPokerAdminPermission(sessionIdentifier) || pokerPlayer.getOwners().contains(authenticationService.getCurrentUser(sessionIdentifier));
	}

	public boolean hasPermission(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerIdentifier pokerPlayerIdentifier
	) throws PokerServiceException, LoginRequiredException, AuthenticationServiceException {
		return hasPermission(sessionIdentifier, pokerService.getPlayer(pokerPlayerIdentifier));
	}

	public void expectPermission(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayer pokerPlayer
	) throws LoginRequiredException, PermissionDeniedException, PokerServiceException, AuthenticationServiceException {
		if (!hasPermission(sessionIdentifier, pokerPlayer)) {
			throw new PermissionDeniedException("only pokerAdmin or owner allowed");
		}
	}

	public void expectPermission(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerIdentifier pokerPlayerIdentifier
	) throws LoginRequiredException, PermissionDeniedException, PokerServiceException, AuthenticationServiceException {
		if (!hasPermission(sessionIdentifier, pokerPlayerIdentifier)) {
			throw new PermissionDeniedException("only pokerAdmin or owner allowed");
		}
	}
}
