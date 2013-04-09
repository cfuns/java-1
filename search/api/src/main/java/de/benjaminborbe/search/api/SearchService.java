package de.benjaminborbe.search.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface SearchService {

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String query, int maxResults) throws SearchServiceException;

	void createHistory(SessionIdentifier sessionIdentifier, String query) throws SearchServiceException;

	List<String> getSearchComponentNames() throws SearchServiceException;

}
