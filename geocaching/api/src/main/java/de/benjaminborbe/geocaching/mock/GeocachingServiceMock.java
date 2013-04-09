package de.benjaminborbe.geocaching.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.geocaching.api.GeocachingService;

@Singleton
public class GeocachingServiceMock implements GeocachingService {

	@Inject
	public GeocachingServiceMock() {
	}

	@Override
	public void execute() {
	}
}
