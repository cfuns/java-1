package de.benjaminborbe.distributed.index.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.distributed.index.gui.DistributedIndexGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class DistributedIndexGuiNavigationEntry implements NavigationEntry {

	private final AuthenticationService authenticationService;

	@Inject
	public DistributedIndexGuiNavigationEntry(final AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
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
			return authenticationService.isSuperAdmin(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			return false;
		}
	}

}
