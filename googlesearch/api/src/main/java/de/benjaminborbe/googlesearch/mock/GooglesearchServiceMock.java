package de.benjaminborbe.googlesearch.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.googlesearch.api.GooglesearchService;

@Singleton
public class GooglesearchServiceMock implements GooglesearchService {

	@Inject
	public GooglesearchServiceMock() {
	}

	@Override
	public void execute() {
	}
}
