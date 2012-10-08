package de.benjaminborbe.confluence.search;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchServiceComponent;

@Singleton
public class ConfluenceSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Confluence";

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final String[] words, final int maxResults) {
		return new ArrayList<SearchResult>();
	}

}
