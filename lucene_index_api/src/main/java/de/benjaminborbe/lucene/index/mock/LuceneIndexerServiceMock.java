package de.benjaminborbe.lucene.index.mock;

import java.net.URL;
import java.util.List;

import de.benjaminborbe.lucene.index.api.LuceneIndexSearchResult;
import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.api.LuceneIndexServiceException;

public class LuceneIndexerServiceMock implements LuceneIndexService {

	@Override
	public void addToLuceneIndex(final String index, final URL url, final String title, final String content) {
	}

	@Override
	public void clear(final String indexName) {
	}

	@Override
	public List<LuceneIndexSearchResult> search(final String index, final String searchQuery, final int limit) throws LuceneIndexServiceException {
		return null;
	}

}
