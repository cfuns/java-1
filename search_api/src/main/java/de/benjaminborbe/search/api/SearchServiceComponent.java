package de.benjaminborbe.search.api;

import java.util.List;

public interface SearchServiceComponent {

	List<SearchResult> search(String[] words, int maxResults);
}
