package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceService extends ConfluenceServiceInstance, ConfluenceServiceSpace {

	boolean refreshSearchIndex(SessionIdentifier sessionIdentifier) throws LoginRequiredException, ConfluenceServiceException, PermissionDeniedException;

	void expireAll(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException;

}
