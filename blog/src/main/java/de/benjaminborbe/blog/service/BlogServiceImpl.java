package de.benjaminborbe.blog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.api.BlogService;

@Singleton
public class BlogServiceImpl implements BlogService {

	private final Logger logger;

	@Inject
	public BlogServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
