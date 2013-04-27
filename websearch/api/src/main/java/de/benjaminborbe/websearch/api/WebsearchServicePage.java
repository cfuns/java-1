package de.benjaminborbe.websearch.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.net.URL;
import java.util.Collection;

public interface WebsearchServicePage {

	WebsearchPageIdentifier createPageIdentifier(URL id) throws WebsearchServiceException;

	WebsearchPageIdentifier createPageIdentifier(String id) throws WebsearchServiceException;

	Collection<WebsearchPage> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException;

	void refreshPage(final SessionIdentifier sessionIdentifier, WebsearchPageIdentifier page) throws WebsearchServiceException, PermissionDeniedException;

	void expirePage(final SessionIdentifier sessionIdentifier, WebsearchPageIdentifier page) throws WebsearchServiceException, PermissionDeniedException;

	void expireAllPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException, LoginRequiredException;

}
