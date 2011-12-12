package de.benjaminborbe.search.util;

import java.util.Collection;

import de.benjaminborbe.search.api.SearchServiceComponent;

public interface SearchServiceComponentRegistry {

	void register(SearchServiceComponent searchServiceComponent);

	void unregister(SearchServiceComponent searchServiceComponent);

	Collection<SearchServiceComponent> getAll();
}
