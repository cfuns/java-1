package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.DhlIdentifier;

public interface DhlStatusStorage {

	void store(DhlStatus dhlStatus);

	DhlStatus get(DhlIdentifier dhlIdentifier);
}
