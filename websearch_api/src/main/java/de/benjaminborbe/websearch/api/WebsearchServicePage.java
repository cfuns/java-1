package de.benjaminborbe.websearch.api;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface WebsearchServicePage {

	WebsearchPageIdentifier createPageIdentifier(URL id) throws WebsearchServiceException;

	Collection<WebsearchPage> getPages(final SessionIdentifier sessionIdentifier, int limit) throws WebsearchServiceException, PermissionDeniedException;

	void refreshPage(final SessionIdentifier sessionIdentifier, WebsearchPageIdentifier page) throws WebsearchServiceException, PermissionDeniedException;

	void expirePage(final SessionIdentifier sessionIdentifier, WebsearchPageIdentifier page) throws WebsearchServiceException, PermissionDeniedException;

	void expireAllPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException, LoginRequiredException;

}
