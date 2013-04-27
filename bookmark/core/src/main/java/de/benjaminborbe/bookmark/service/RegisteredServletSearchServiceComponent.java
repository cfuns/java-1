package de.benjaminborbe.bookmark.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.search.SearchUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

	private final SearchUtil searchUtil;

	@Inject
	public RegisteredServletSearchServiceComponent(final Logger logger, final ServletPathRegistry servletPathRegistry, final SearchUtil searchUtil) {
		this.logger = logger;
		this.servletPathRegistry = servletPathRegistry;
		this.searchUtil = searchUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		final List<String> words = searchUtil.buildSearchParts(query);
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<>();
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

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList("servlet");
	}
}
