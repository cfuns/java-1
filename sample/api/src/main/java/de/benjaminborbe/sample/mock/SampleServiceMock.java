package de.benjaminborbe.sample.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.sample.api.SampleService;

@Singleton
public class SampleServiceMock implements SampleService {

	@Inject
	public SampleServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
