package de.benjaminborbe.index.api;

import java.util.List;

public class IndexSearcherServiceMock implements IndexSearcherService {

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery) {
		return null;
	}

}
