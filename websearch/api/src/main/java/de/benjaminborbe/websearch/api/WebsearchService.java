package de.benjaminborbe.websearch.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface WebsearchService extends WebsearchServicePage, WebsearchServiceConfiguration {

	void saveImages(SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	boolean refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	WebsearchPage getPage(
		SessionIdentifier sessionIdentifier,
		WebsearchPageIdentifier websearchPageIdentifier
	) throws WebsearchServiceException, PermissionDeniedException;
}
