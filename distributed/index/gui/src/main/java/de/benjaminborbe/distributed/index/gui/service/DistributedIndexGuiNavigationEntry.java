package de.benjaminborbe.distributed.index.gui.service;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.distributed.index.gui.DistributedIndexGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class DistributedIndexGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public DistributedIndexGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "DistributedIndex";
	}

	@Override
	public String getURL() {
		return "/" + DistributedIndexGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authorizationService.hasAdminRole(sessionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
