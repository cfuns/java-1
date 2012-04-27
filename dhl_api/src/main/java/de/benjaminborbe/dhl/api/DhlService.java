package de.benjaminborbe.dhl.api;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface DhlService {

	URL buildDhlUrl(DhlIdentifier dhlIdentifier) throws DhlServiceException;

	DhlIdentifier createDhlIdentifier(SessionIdentifier sessionIdentifier, long id, long zip) throws DhlServiceException;

	void mailStatus(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException;

	boolean removeTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	boolean addTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;
}
