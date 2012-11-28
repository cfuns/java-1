package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceService extends ConfluenceServiceInstance, ConfluenceServiceSpace {

	void refreshSearchIndex(SessionIdentifier sessionIdentifier) throws LoginRequiredException, ConfluenceServiceException, PermissionDeniedException;

}
