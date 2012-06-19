package de.benjaminborbe.lunch.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.lunch.api.LunchService;

@Singleton
public class LunchServiceMock implements LunchService {

	@Inject
	public LunchServiceMock() {
	}

	@Override
	public void execute() {
	}
}
