package de.benjaminborbe.lucene.index.api;

import java.net.URL;

public class LuceneIndexerServiceMock implements LuceneIndexerService {

	@Override
	public void addToLuceneIndex(final String index, final URL url, final String title, final String content) {
	}

	@Override
	public void clear(final String indexName) {
	}

}
