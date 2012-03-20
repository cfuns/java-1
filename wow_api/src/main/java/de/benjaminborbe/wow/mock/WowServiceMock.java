package de.benjaminborbe.wow.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wow.api.WowService;

@Singleton
public class WowServiceMock implements WowService {

	@Inject
	public WowServiceMock() {
	}

	@Override
	public void execute() {
	}
}
