package de.benjaminborbe.search.core.util;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.core.config.SearchConfig;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UrlSearchServiceComponent implements SearchServiceComponent {

	private final Logger logger;

	private final SearchConfig searchConfig;

	@Inject
	public UrlSearchServiceComponent(final Logger logger, final SearchConfig searchConfig) {
		this.logger = logger;
		this.searchConfig = searchConfig;
	}

	@Override
	public String getName() {
		return "URL";
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		logger.trace("search");
		final List<SearchResult> result = new ArrayList<>();
		try {
			if (query != null && searchConfig.isUrlSearchActive()) {
				final String queryTrimed = query.trim();
				if (!queryTrimed.isEmpty() && !queryTrimed.contains(" ")) {
					final String urlString = new URL("http://" + query).toExternalForm();
					final String type = getName();
					final String title = urlString;
					final String description = urlString;
					result.add(new SearchResultImpl(type, Integer.MAX_VALUE, title, urlString, description));
				}
			}
		} catch (final MalformedURLException e) {
			// nop
		}
		return result;
	}

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList();
	}
}
