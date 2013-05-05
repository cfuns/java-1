package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceService extends ConfluenceServiceInstance, ConfluenceServiceSpace, ConfluenceServicePage {

	String PERMISSION = "confluence";

	void expectPermission(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException;

	boolean hasPermission(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException;

	void expireAll(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException;

}
