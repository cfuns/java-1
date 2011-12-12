package de.benjaminborbe.search.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchServiceComponent;

@Singleton
public class SearchServiceComponentRegistryImpl implements SearchServiceComponentRegistry {

	private final Set<SearchServiceComponent> searchServiceComponents = new HashSet<SearchServiceComponent>();

	@Override
	public void register(final SearchServiceComponent searchServiceComponent) {
		searchServiceComponents.add(searchServiceComponent);
	}

	@Override
	public void unregister(final SearchServiceComponent searchServiceComponent) {
		searchServiceComponents.remove(searchServiceComponent);
	}

	@Override
	public Collection<SearchServiceComponent> getAll() {
		return searchServiceComponents;
	}
}
