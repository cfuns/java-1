package de.benjaminborbe.confluence.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.confluence.ConfluenceConstants;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;

@Singleton
public class ConfluenceSearchServiceComponent implements SearchServiceComponent {

	private final class BeanSearcherImpl extends BeanSearcher<IndexSearchResult> {

		@Override
		protected Collection<String> getSearchValues(final IndexSearchResult bean) {
			final List<String> values = new ArrayList<String>();
			values.add(bean.getContent());
			values.add(bean.getContent());
			values.add(bean.getURL());
			return values;
		}
	}

	private static final String SEARCH_TYPE = "Confluence";

	private final Logger logger;

	private final IndexSearcherService indexSearcherService;

	@Inject
	public ConfluenceSearchServiceComponent(final Logger logger, final IndexSearcherService indexSearcherService) {
		this.logger = logger;
		this.indexSearcherService = indexSearcherService;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final String... words) {
		logger.trace("search");
		final List<IndexSearchResult> indexResults = indexSearcherService.search(ConfluenceConstants.INDEX, StringUtils.join(words, " "));
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
}
