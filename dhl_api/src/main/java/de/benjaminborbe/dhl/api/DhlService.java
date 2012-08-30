package de.benjaminborbe.dhl.api;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface DhlService {

	URL buildDhlUrl(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	DhlIdentifier createDhlIdentifier(SessionIdentifier sessionIdentifier, long id) throws DhlServiceException;

	void mailStatus(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	Collection<DhlIdentifier> getRegisteredDhlIdentifiers(final SessionIdentifier sessionIdentifier) throws DhlServiceException;

	void removeTracking(SessionIdentifier sessionIdentifier, DhlIdentifier dhlIdentifier) throws DhlServiceException;

	DhlIdentifier addTracking(SessionIdentifier sessionIdentifier, long trackingNumber, long zip) throws DhlServiceException;
}
