package de.benjaminborbe.sample.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SampleServiceMock implements SampleService {

	@Inject
	public SampleServiceMock() {
	}

	@Override
	public void execute() {
	}
}
