package de.benjaminborbe.confluence.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceServiceInstance {

	ConfluenceInstanceIdentifier createConfluenceInstanceIdentifier(SessionIdentifier sessionIdentifier, String id) throws ConfluenceServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	Collection<ConfluenceInstance> getConfluenceInstances(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException,
			SuperAdminRequiredException;

	ConfluenceInstanceIdentifier createConfluenceIntance(final SessionIdentifier sessionIdentifier, String url, String username, String password) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	void updateConfluenceIntance(final SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier, String url, String username, String password)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	void deleteConfluenceInstance(final SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException, SuperAdminRequiredException;

	Collection<ConfluenceInstanceIdentifier> getConfluenceInstanceIdentifiers(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	ConfluenceInstance getConfluenceInstance(SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException, SuperAdminRequiredException;

}
