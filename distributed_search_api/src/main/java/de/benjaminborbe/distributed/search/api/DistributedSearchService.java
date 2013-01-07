package de.benjaminborbe.distributed.search.api;

import java.net.URL;
import java.util.List;

public interface DistributedSearchService {

	void addToIndex(String index, URL url, String title, String content) throws DistributedSearchServiceException;

	void clear(String index) throws DistributedSearchServiceException;

	List<DistributedSearchResult> search(String index, String searchQuery, int limit) throws DistributedSearchServiceException;

	void removeFromIndex(String index, URL url) throws DistributedSearchServiceException;

	void rebuildIndex(String index) throws DistributedSearchServiceException;

	DistributedSearchResult getPage(String index, String url) throws DistributedSearchServiceException;

}
