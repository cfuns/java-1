package de.benjaminborbe.search.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

import java.util.Collection;
import java.util.List;

public interface SearchServiceComponent {

	String getName();

	Collection<String> getAliases();

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String query, int maxResults);
}
