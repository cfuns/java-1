package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.websearch.core.WebsearchConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class WebsearchSearchServiceComponent implements SearchServiceComponent {

	private final class BeanSearcherImpl extends BeanSearcher<IndexSearchResult> {

		private static final String URL = "url";

		private static final String CONTENT = "content";

		private static final String TITLE = "title";

		@Override
		protected Map<String, String> getSearchValues(final IndexSearchResult bean) {
			final Map<String, String> values = new HashMap<String, String>();
			values.put(TITLE, bean.getTitle());
			values.put(CONTENT, bean.getContent());
			values.put(URL, bean.getURL());
			return values;
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			final Map<String, Integer> values = new HashMap<String, Integer>();
			values.put(TITLE, 2);
			values.put(CONTENT, 1);
			values.put(URL, 2);
			return values;
		}
	}

	private static final String SEARCH_TYPE = "Web";

	private final Logger logger;

	private final IndexService indexSearcherService;

	private final SearchUtil searchUtil;

	@Inject
	public WebsearchSearchServiceComponent(final Logger logger, final IndexService indexSearcherService, final SearchUtil searchUtil) {
		this.logger = logger;
		this.indexSearcherService = indexSearcherService;
		this.searchUtil = searchUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		logger.debug("search - query: " + query);
		final List<SearchResult> result = new ArrayList<SearchResult>();
		try {
			final List<String> words = searchUtil.buildSearchParts(query);
			final List<IndexSearchResult> indexResults = indexSearcherService.search(WebsearchConstants.INDEX, StringUtils.join(words, " "), WebsearchConstants.SEARCH_LIMIT);
			final BeanSearcher<IndexSearchResult> beanSearcher = new BeanSearcherImpl();
			final List<BeanMatch<IndexSearchResult>> beanResults = beanSearcher.search(indexResults, maxResults, words);
			for (final BeanMatch<IndexSearchResult> beanResult : beanResults) {
				if (result.size() < maxResults) {
					result.add(map(beanResult));
				}
			}
			logger.debug("search - found " + result.size() + " results");
		} catch (final IndexerServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		return result;
	}

	protected SearchResult map(final BeanMatch<IndexSearchResult> beanResult) {
		final IndexSearchResult bean = beanResult.getBean();
		final String title = bean.getTitle();
		final String url = bean.getURL();
		final String description = bean.getContent();
		final int matchCounter = beanResult.getMatchCounter();
		return new SearchResultImpl(SEARCH_TYPE, matchCounter, title, url, description);
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList("web");
	}
}
