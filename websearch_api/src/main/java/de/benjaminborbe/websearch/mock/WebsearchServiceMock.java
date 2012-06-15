package de.benjaminborbe.websearch.mock;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.PageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;

public class WebsearchServiceMock implements WebsearchService {

	@Override
	public Collection<Page> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void refreshPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public void expirePage(final SessionIdentifier sessionIdentifier, final PageIdentifier page) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

}
