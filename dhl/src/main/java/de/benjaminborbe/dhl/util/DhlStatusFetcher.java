package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.DhlIdentifier;

public interface DhlStatusFetcher {

	DhlStatus fetchStatus(DhlIdentifier dhlIdentifier) throws DhlStatusFetcherException;
}
