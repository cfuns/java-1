package de.benjaminborbe.shortener.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.shortener.api.ShortenerService;

@Singleton
public class ShortenerServiceMock implements ShortenerService {

	@Inject
	public ShortenerServiceMock() {
	}

	@Override
	public void execute() {
	}
}
