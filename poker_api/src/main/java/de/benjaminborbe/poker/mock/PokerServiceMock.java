package de.benjaminborbe.poker.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerService;

@Singleton
public class PokerServiceMock implements PokerService {

	@Inject
	public PokerServiceMock() {
	}

	@Override
	public void execute() {
	}
}
