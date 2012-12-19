package de.benjaminborbe.confluence.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceServiceInstance {

	ConfluenceInstanceIdentifier createConfluenceInstanceIdentifier(SessionIdentifier sessionIdentifier, String id) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException;

	Collection<ConfluenceInstance> getConfluenceInstances(SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException;

	ConfluenceInstanceIdentifier createConfluenceIntance(final SessionIdentifier sessionIdentifier, String url, String username, String password, int expire, boolean shared,
			long delay, boolean activated) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	void updateConfluenceIntance(final SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier, String url, String username, String password,
			int expire, boolean shared, long delay, boolean activated) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	void deleteConfluenceInstance(final SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException;

	Collection<ConfluenceInstanceIdentifier> getConfluenceInstanceIdentifiers(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException;

	ConfluenceInstance getConfluenceInstance(SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException;

	long countPages(SessionIdentifier sessionIdentifier, ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException;

}
