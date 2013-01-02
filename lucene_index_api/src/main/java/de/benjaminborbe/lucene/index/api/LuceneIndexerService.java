package de.benjaminborbe.lucene.index.api;

import java.net.URL;

public interface LuceneIndexerService {

	void addToLuceneIndex(String index, URL url, String title, String content) throws LuceneIndexerServiceException;

	void clear(String indexName) throws LuceneIndexerServiceException;
}
