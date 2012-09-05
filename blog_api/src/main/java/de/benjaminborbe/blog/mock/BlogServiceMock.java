package de.benjaminborbe.blog.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.api.BlogService;

@Singleton
public class BlogServiceMock implements BlogService {

	@Inject
	public BlogServiceMock() {
	}

	@Override
	public void execute() {
	}
}
