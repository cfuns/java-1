package de.benjaminborbe.forum.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.forum.api.ForumService;

@Singleton
public class ForumServiceMock implements ForumService {

	@Inject
	public ForumServiceMock() {
	}

	@Override
	public void execute() {
	}
}
