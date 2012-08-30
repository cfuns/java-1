package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.Dhl;

public interface DhlStatusFetcher {

	DhlStatus fetchStatus(Dhl dhl) throws DhlStatusFetcherException;
}
