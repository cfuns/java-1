package de.benjaminborbe.search.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface SearchService {

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String query, int maxResults);

	List<String> getSearchComponentNames();
}
