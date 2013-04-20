package de.benjaminborbe.crawler.gui.service;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.crawler.gui.CrawlerGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class CrawlerGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public CrawlerGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Crawler";
	}

	@Override
	public String getURL() {
		return "/" + CrawlerGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasAdminRole(sessionIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
