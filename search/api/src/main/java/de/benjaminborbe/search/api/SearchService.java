package de.benjaminborbe.search.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

import java.util.List;

public interface SearchService {

	List<SearchResult> search(SessionIdentifier sessionIdentifier, String query, int maxResults) throws SearchServiceException;

	void createHistory(SessionIdentifier sessionIdentifier, String query) throws SearchServiceException;

	List<String> getSearchComponentNames() throws SearchServiceException;

}
