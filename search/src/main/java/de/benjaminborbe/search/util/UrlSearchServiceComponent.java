package de.benjaminborbe.search.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;

public class UrlSearchServiceComponent implements SearchServiceComponent {

	private final Logger logger;

	@Inject
	public UrlSearchServiceComponent(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getName() {
		return "URL";
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final String[] words, final int maxResults) {
		logger.trace("search");
		final List<SearchResult> result = new ArrayList<SearchResult>();
		if (query != null && query.trim().length() > 0) {
			final String urlString = "http://" + query;
			final String type = getName();
			final String title = urlString;
			final String description = urlString;
			result.add(new SearchResultImpl(type, title, urlString, description));
		}
		return result;
	}
}
