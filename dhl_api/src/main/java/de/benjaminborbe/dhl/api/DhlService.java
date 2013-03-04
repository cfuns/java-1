package de.benjaminborbe.dhl.api;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface DhlService {

	String PERMISSION = "dhl";

	DhlIdentifier createDhlIdentifier(String id) throws DhlServiceException;

	URL buildDhlUrl(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;

	void mailStatus(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<DhlIdentifier> getIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Dhl> getEntries(final SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;

	void removeTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;

	DhlIdentifier addTracking(SessionIdentifier sessionIdentifier, String trackingNumber, long zip) throws DhlServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	void triggerCheck(SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;
}
