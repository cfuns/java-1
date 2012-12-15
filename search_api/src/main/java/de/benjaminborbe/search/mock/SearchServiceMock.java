package de.benjaminborbe.search.mock;

import java.util.ArrayList;
import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

public class SearchServiceMock implements SearchService {

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final List<String> words) {
		return new ArrayList<SearchResult>();
	}

	@Override
	public List<String> getSearchComponentNames() {
		return new ArrayList<String>();
	}

	@Override
	public String getName() {
		return null;
	}

}
