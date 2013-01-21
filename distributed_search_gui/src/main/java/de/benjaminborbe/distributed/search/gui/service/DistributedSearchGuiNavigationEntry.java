package de.benjaminborbe.distributed.search.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.distributed.search.gui.DistributedSearchGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class DistributedSearchGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public DistributedSearchGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String getTitle() {
		return "DistributedSearch";
	}

	@Override
	public String getURL() {
		return "/" + DistributedSearchGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return authenticationService.isSuperAdmin(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			return false;
		}
	}

}
