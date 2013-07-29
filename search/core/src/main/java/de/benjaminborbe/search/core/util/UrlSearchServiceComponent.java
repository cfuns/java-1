package de.benjaminborbe.search.core.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.core.config.SearchConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UrlSearchServiceComponent implements SearchServiceComponent {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final SearchConfig searchConfig;

	@Inject
	public UrlSearchServiceComponent(final Logger logger, final ParseUtil parseUtil, final SearchConfig searchConfig) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.searchConfig = searchConfig;
	}

	@Override
	public String getName() {
		return "URL";
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		logger.trace("search");
		final List<SearchResult> result = new ArrayList<SearchResult>();
		try {
			if (query != null && searchConfig.isUrlSearchActive()) {
				final String queryTrimed = query.trim();
				if (!queryTrimed.isEmpty() && !queryTrimed.contains(" ")) {
					final String urlString = parseUtil.parseURL("http://" + query).toExternalForm();
					final String type = getName();
					result.add(new SearchResultImpl(type, Integer.MAX_VALUE, urlString, urlString, urlString));
				}
			}
		} catch (ParseException e) {
			// nop
		}
		return result;
	}

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList();
	}
}
