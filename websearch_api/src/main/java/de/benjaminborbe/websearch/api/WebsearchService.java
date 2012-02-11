package de.benjaminborbe.websearch.api;

import java.util.Collection;

public interface WebsearchService {

	Collection<Page> getPages() throws WebsearchServiceException;

	void refreshPages() throws WebsearchServiceException;

	void expirePage(PageIdentifier page) throws WebsearchServiceException;

}
