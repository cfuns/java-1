package de.benjaminborbe.distributed.search.api;

import java.net.URL;
import java.util.List;

public interface DistributedSearchService {

	void addToIndex(String index, URL url, String title, String content) throws DistributedSearchServiceException;

	void clear(String indexName) throws DistributedSearchServiceException;

	List<DistributedSearchResult> search(String index, String searchQuery, int limit) throws DistributedSearchServiceException;
}
