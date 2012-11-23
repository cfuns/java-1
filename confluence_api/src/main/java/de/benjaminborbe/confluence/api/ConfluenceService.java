package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;

public interface ConfluenceService extends ConfluenceServiceInstance, ConfluenceServiceSpace {

	void refreshSearchIndex(SessionIdentifier sessionIdentifier) throws LoginRequiredException, SuperAdminRequiredException, ConfluenceServiceException;

}
