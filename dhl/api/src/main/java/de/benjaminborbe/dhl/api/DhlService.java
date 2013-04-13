package de.benjaminborbe.dhl.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.net.URL;
import java.util.Collection;

public interface DhlService {

	String PERMISSION = "dhl";

	DhlIdentifier createDhlIdentifier(String id);

	URL buildDhlUrl(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, PermissionDeniedException;

	void notifyStatus(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, PermissionDeniedException;

	Collection<Dhl> getEntries(final SessionIdentifier sessionIdentifier) throws DhlServiceException, PermissionDeniedException;

	void removeTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException, PermissionDeniedException;

	DhlIdentifier addTracking(SessionIdentifier sessionIdentifier, String trackingNumber, long zip) throws DhlServiceException, PermissionDeniedException,
		ValidationException;

	void triggerCheck(SessionIdentifier sessionIdentifier) throws DhlServiceException, LoginRequiredException, PermissionDeniedException;
}
