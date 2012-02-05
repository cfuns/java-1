package de.benjaminborbe.search.mock;

import java.util.ArrayList;
import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

public class SearchServiceMock implements SearchService {

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String[] words, final int maxResults) {
		return new ArrayList<SearchResult>();
	}

	@Override
	public List<String> getSearchComponentNames() {
		return new ArrayList<String>();
	}

}
