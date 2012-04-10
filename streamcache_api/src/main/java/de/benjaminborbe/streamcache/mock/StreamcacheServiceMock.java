package de.benjaminborbe.streamcache.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.streamcache.api.StreamcacheService;

@Singleton
public class StreamcacheServiceMock implements StreamcacheService {

	@Inject
	public StreamcacheServiceMock() {
	}

	@Override
	public void execute() {
	}
}
