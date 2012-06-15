package de.benjaminborbe.websearch.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface WebsearchService {

	Collection<Page> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	void refreshPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	void expirePage(final SessionIdentifier sessionIdentifier, PageIdentifier page) throws WebsearchServiceException, PermissionDeniedException;

	void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

}
