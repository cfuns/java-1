package de.benjaminborbe.websearch.mock;

import java.util.Collection;

import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.PageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;

public class WebsearchServiceMock implements WebsearchService {

	@Override
	public Collection<Page> getPages() {
		return null;
	}

	@Override
	public void refreshPages() {
	}

	@Override
	public void expirePage(final PageIdentifier page) throws WebsearchServiceException {

	}

}
