package de.benjaminborbe.confluence.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.ConfluenceConstants;
import de.benjaminborbe.confluence.util.ConfluenceIndexUtil;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.search.SearchUtil;

@Singleton
public class ConfluenceSearchServiceComponent implements SearchServiceComponent {

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

	private static final String SEARCH_TYPE = "Confluence";

	private final Logger logger;

	private final IndexService indexSearcherService;

	private final AuthenticationService authenticationService;

	private final ConfluenceIndexUtil confluenceIndexUtil;

	private final SearchUtil searchUtil;

	@Inject
	public ConfluenceSearchServiceComponent(
			final Logger logger,
			final SearchUtil searchUtil,
			final IndexService indexSearcherService,
			final AuthenticationService authenticationService,
			final ConfluenceIndexUtil confluenceIndexUtil) {
		this.logger = logger;
		this.searchUtil = searchUtil;
		this.indexSearcherService = indexSearcherService;
		this.authenticationService = authenticationService;
		this.confluenceIndexUtil = confluenceIndexUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		final List<IndexSearchResult> indexResults = new ArrayList<IndexSearchResult>();
		final List<String> words = searchUtil.buildSearchParts(query);
		try {
			logger.trace("search");
			final String searchString = StringUtils.join(words, " ");
			indexResults.addAll(indexSearcherService.search(confluenceIndexUtil.indexShared(), searchString, ConfluenceConstants.SEARCH_LIMIT));
			try {
				final UserIdentifier user = authenticationService.getCurrentUser(sessionIdentifier);
				indexResults.addAll(indexSearcherService.search(confluenceIndexUtil.indexPrivate(user), searchString, ConfluenceConstants.SEARCH_LIMIT));
			}
			catch (final AuthenticationServiceException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
		catch (final IndexerServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}

		final BeanSearcher<IndexSearchResult> beanSearcher = new BeanSearcherImpl();
		final List<BeanMatch<IndexSearchResult>> beanResults = beanSearcher.search(indexResults, maxResults, words);
		final List<SearchResult> result = new ArrayList<SearchResult>();
		for (final BeanMatch<IndexSearchResult> beanResult : beanResults) {
			if (result.size() < maxResults) {
				result.add(map(beanResult));
			}
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
		return Arrays.asList("confluence", "c");
	}
}
