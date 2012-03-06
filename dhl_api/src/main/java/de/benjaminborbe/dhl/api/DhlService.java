package de.benjaminborbe.dhl.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface DhlService {

	DhlIdentifier createDhlIdentifier(SessionIdentifier sessionIdentifier, long id, long zip) throws DhlServiceException;

	void mailStatus(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException;

	boolean removeTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	boolean addTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;
}
