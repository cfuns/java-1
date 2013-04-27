package de.benjaminborbe.poker.gui.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;

import javax.inject.Inject;

public class PokerGuiNavigationEntry implements NavigationEntry {

	private final PokerService pokerService;

	@Inject
	public PokerGuiNavigationEntry(final PokerService pokerService) {
		this.pokerService = pokerService;
	}

	@Override
	public String getTitle() {
		return "Poker";
	}

	@Override
	public String getURL() {
		return "/" + PokerGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return pokerService.hasPokerPlayerOrAdminPermission(sessionIdentifier);
		} catch (final LoginRequiredException e) {
			return false;
		} catch (final PokerServiceException e) {
			return false;
		}
	}

}
