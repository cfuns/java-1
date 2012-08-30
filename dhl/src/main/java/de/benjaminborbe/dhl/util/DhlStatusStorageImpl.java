package de.benjaminborbe.dhl.util;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;

@Singleton
public class DhlStatusStorageImpl implements DhlStatusStorage {

	private final Map<DhlIdentifier, DhlStatus> data = new HashMap<DhlIdentifier, DhlStatus>();

	@Override
	public void store(final DhlStatus dhlStatus) {
		if (dhlStatus == null) {
			throw new NullPointerException("parameter DhlStatus missing");
		}
		data.put(dhlStatus.getDhl().getId(), dhlStatus);
	}

	@Override
	public DhlStatus get(final DhlIdentifier dhlIdentifier) {
		return data.get(dhlIdentifier);
	}

}
