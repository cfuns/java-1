package de.benjaminborbe.search.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface SearchServiceComponent {

	String getName();

	Collection<String> getAliases();

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String query, int maxResults);
}
