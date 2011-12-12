package de.benjaminborbe.search.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

@Singleton
public class SearchServiceImpl implements SearchService {

	private final Logger logger;

	@Inject
	public SearchServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<SearchResult> search(final String queryString) {
		logger.debug("search queryString: " + queryString);

		final List<SearchResult> result = new ArrayList<SearchResult>();
		return result;
	}
}
