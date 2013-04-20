package de.benjaminborbe.googlesearch.service;

import java.util.List;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.googlesearch.api.GooglesearchService;

@Singleton
public class GooglesearchServiceImpl implements GooglesearchService {

	private final Logger logger;

	@Inject
	public GooglesearchServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

	public List<String> getSuggestions() {
		// http://suggestqueries.google.com/complete/search?output=firefox&client=firefox&hl=en&q=KEYWORD
		return null;
	}

}
