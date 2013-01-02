package de.benjaminborbe.lucene.index.api;

import java.util.List;

public class LuceneIndexSearcherServiceMock implements LuceneIndexSearcherService {

	@Override
	public List<LuceneIndexSearchResult> search(final String index, final String searchQuery, final int limit) {
		return null;
	}

}
