package de.benjaminborbe.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;

@Singleton
public class SearchServiceImpl implements SearchService {

	private final Logger logger;

	private final SearchServiceComponentRegistry searchServiceComponentRegistry;

	@Inject
	public SearchServiceImpl(final Logger logger, final SearchServiceComponentRegistry searchServiceComponentRegistry) {
		this.logger = logger;
		this.searchServiceComponentRegistry = searchServiceComponentRegistry;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String[] words, final int maxResults) {
		logger.debug("search words: " + StringUtils.join(words, ","));

		final List<SearchResult> result = new ArrayList<SearchResult>();
		final Collection<SearchServiceComponent> searchServiceComponents = searchServiceComponentRegistry.getAll();
		logger.debug("searchServiceComponents " + searchServiceComponents.size());
		for (final SearchServiceComponent searchServiceComponent : searchServiceComponents) {
			logger.debug("search in searchServiceComponent: " + searchServiceComponent.getClass().getSimpleName());
			try {
				result.addAll(searchServiceComponent.search(sessionIdentifier, words, maxResults));
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
		logger.debug("found " + result.size() + " results");
		return result;
	}

	@Override
	public List<String> getSearchComponentNames() {
		final List<String> result = new ArrayList<String>();
		for (final SearchServiceComponent cs : searchServiceComponentRegistry.getAll()) {
			result.add(cs.getClass().getName());
		}
		return null;
	}

}
