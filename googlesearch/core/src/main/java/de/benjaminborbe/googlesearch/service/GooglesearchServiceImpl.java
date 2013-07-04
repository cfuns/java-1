package de.benjaminborbe.googlesearch.service;

import de.benjaminborbe.googlesearch.api.GooglesearchService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

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
