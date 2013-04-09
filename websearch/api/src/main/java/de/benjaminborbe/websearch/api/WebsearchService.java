package de.benjaminborbe.websearch.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface WebsearchService extends WebsearchServicePage, WebsearchServiceConfiguration {

	void refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

}
