package de.benjaminborbe.sample.mock;

import de.benjaminborbe.sample.api.SampleService;

public class SampleServiceMock implements SampleService {

	public SampleServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
