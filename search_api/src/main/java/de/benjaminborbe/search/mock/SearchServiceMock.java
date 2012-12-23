package de.benjaminborbe.search.mock;

import java.util.Arrays;
import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;

public class SearchServiceMock implements SearchService {

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		return Arrays.asList();
	}

	@Override
	public List<String> getSearchComponentNames() {
		return Arrays.asList();
	}

}
