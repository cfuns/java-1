package de.benjaminborbe.search.api;

import java.util.List;

public interface SearchService {

	List<SearchResult> search(String queryString, int maxResults);
}
