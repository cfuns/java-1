package de.benjaminborbe.index.api;

import java.net.URL;
import java.util.List;

public interface IndexService {

	void addToIndex(String index, URL url, String title, String content) throws IndexerServiceException;

	void removeFromIndex(String index, URL url) throws IndexerServiceException;

	void clear(String indexName) throws IndexerServiceException;

	List<IndexSearchResult> search(String index, String searchQuery, int limit) throws IndexerServiceException;

}
