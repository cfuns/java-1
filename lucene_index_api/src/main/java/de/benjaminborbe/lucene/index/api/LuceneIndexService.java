package de.benjaminborbe.lucene.index.api;

import java.net.URL;
import java.util.List;

public interface LuceneIndexService {

	void addToIndex(String index, URL url, String title, String content) throws LuceneIndexServiceException;

	void removeFromIndex(String index, URL url) throws LuceneIndexServiceException;

	void clear(String indexName) throws LuceneIndexServiceException;

	List<LuceneIndexSearchResult> search(String index, String searchQuery, int limit) throws LuceneIndexServiceException;

}
