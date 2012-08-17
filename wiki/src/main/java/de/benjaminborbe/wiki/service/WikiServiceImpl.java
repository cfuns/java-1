package de.benjaminborbe.wiki.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiService;

@Singleton
public class WikiServiceImpl implements WikiService {

	private final Logger logger;

	@Inject
	public WikiServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
