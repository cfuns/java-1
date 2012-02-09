package de.benjaminborbe.websearch.mock;

import java.util.Collection;

import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.WebsearchService;

public class WebsearchServiceMock implements WebsearchService {

	@Override
	public Collection<Page> getPages() {
		return null;
	}

	@Override
	public void refreshPages() {
	}

}
