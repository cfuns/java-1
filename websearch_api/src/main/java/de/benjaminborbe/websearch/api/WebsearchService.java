package de.benjaminborbe.websearch.api;

import java.util.Collection;

public interface WebsearchService {

	Collection<Page> getPages();

	void refreshPages();

}
