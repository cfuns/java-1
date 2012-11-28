package de.benjaminborbe.confluence.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceServiceSpace {

	Collection<ConfluenceSpaceIdentifier> getConfluenceSpaceIdentifiers(SessionIdentifier sessionIdentifier, String confluenceUrl, String username, String password)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException;

}
