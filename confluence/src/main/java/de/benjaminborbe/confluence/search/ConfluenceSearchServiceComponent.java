package de.benjaminborbe.confluence.search;

import java.util.ArrayList;
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

@Singleton
public class ConfluenceSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Confluence";

	private final Logger logger;

	private final IndexSearcherService indexSearcherService;

	@Inject
	public ConfluenceSearchServiceComponent(final Logger logger, final IndexSearcherService indexSearcherService) {
		this.logger = logger;
		this.indexSearcherService = indexSearcherService;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final String[] words, final int maxResults) {
		logger.trace("search");
		final List<SearchResult> result = new ArrayList<SearchResult>();
		final List<IndexSearchResult> indexResults = indexSearcherService.search(ConfluenceConstants.INDEX, StringUtils.join(words, " "));
		for (final IndexSearchResult indexResult : indexResults) {
			if (result.size() < maxResults) {
				result.add(map(indexResult));
			}
		}
		return result;
	}

	protected SearchResult map(final IndexSearchResult indexResult) {
		final String title = indexResult.getTitle();
		final String url = indexResult.getURL();
		final String description = buildDescription(indexResult.getContent());
		return new SearchResultImpl(SEARCH_TYPE, title, url, description);
	}

	protected String buildDescription(final String content) {
		return "-";
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}
}
