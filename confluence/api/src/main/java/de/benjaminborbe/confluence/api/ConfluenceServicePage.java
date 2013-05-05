package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface ConfluenceServicePage {

	ConfluencePageIdentifier createConfluencePageIdentifier(String pageId);

	boolean refreshPages(SessionIdentifier sessionIdentifier) throws LoginRequiredException, ConfluenceServiceException, PermissionDeniedException;

	boolean refreshPage(
		SessionIdentifier sessionIdentifier,
		ConfluencePageIdentifier confluencePageIdentifier
	) throws LoginRequiredException, ConfluenceServiceException, PermissionDeniedException;

	ConfluencePageIdentifier findPageByUrl(
		SessionIdentifier sessionIdentifier,
		ConfluenceInstanceIdentifier confluenceInstanceIdentifier,
		String pageUrl
	) throws LoginRequiredException, ConfluenceServiceException, PermissionDeniedException;
}
