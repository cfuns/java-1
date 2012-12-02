package de.benjaminborbe.bookmark.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;

@Singleton
public class RegisteredServletSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Servlet";

	private final Logger logger;

	private final ServletPathRegistry servletPathRegistry;

	@Inject
	public RegisteredServletSearchServiceComponent(final Logger logger, final ServletPathRegistry servletPathRegistry) {
		this.logger = logger;
		this.servletPathRegistry = servletPathRegistry;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final String... words) {
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();

		final BeanSearcher<String> beanSearch = new BeanSearcher<String>() {

			@Override
			protected Collection<String> getSearchValues(final String bean) {
				return Arrays.asList(bean);
			}
		};
		final List<BeanMatch<String>> matches = beanSearch.search(servletPathRegistry.getAll(), maxResults, words);
		for (final BeanMatch<String> match : matches) {
			results.add(new SearchResultImpl(SEARCH_TYPE, match.getMatchCounter(), match.getBean(), match.getBean(), "-"));
		}
		return results;
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}
}
