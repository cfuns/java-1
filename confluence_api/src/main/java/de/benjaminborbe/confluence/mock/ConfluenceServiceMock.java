package de.benjaminborbe.confluence.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;

@Singleton
public class ConfluenceServiceMock implements ConfluenceService {

	@Inject
	public ConfluenceServiceMock() {
	}

	@Override
	public void execute() {
	}
}
