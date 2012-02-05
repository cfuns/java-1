package de.benjaminborbe.search.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface SearchServiceComponent {

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String[] words, int maxResults);
}
