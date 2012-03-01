package de.benjaminborbe.dhl.api;

public interface DhlService {

	void mailStatus(DhlIdentifier dhlIdentifier) throws DhlServiceException;
}
