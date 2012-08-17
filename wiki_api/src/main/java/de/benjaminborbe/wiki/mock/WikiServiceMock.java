package de.benjaminborbe.wiki.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiService;

@Singleton
public class WikiServiceMock implements WikiService {

	@Inject
	public WikiServiceMock() {
	}

	@Override
	public void execute() {
	}
}
