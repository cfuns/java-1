package de.benjaminborbe.bookmark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;

@Singleton
public class RegisteredServletSearchServiceComponent implements SearchServiceComponent {

	private final class BeanSearchImpl extends BeanSearcher<String> {

		private final String FIELD = "field";

		@Override
		protected Map<String, String> getSearchValues(final String bean) {
			return new MapChain<String, String>().add(FIELD, bean);
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			return new MapChain<String, Integer>().add(FIELD, 1);
		}
	}

	private static final String SEARCH_TYPE = "Servlet";

	private final Logger logger;

	private final ServletPathRegistry servletPathRegistry;

	@Inject
	public RegisteredServletSearchServiceComponent(final Logger logger, final ServletPathRegistry servletPathRegistry) {
		this.logger = logger;
		this.servletPathRegistry = servletPathRegistry;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final List<String> words) {
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();
		final BeanSearcher<String> beanSearch = new BeanSearchImpl();
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
