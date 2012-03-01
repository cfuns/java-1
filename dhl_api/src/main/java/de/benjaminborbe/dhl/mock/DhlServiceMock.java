package de.benjaminborbe.dhl.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.api.DhlService;

@Singleton
public class DhlServiceMock implements DhlService {

	@Inject
	public DhlServiceMock() {
	}

	@Override
	public void mailStatus(final DhlIdentifier dhlIdentifier) {
	}

}
