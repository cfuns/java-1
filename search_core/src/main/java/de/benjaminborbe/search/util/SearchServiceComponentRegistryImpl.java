package de.benjaminborbe.search.util;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class SearchServiceComponentRegistryImpl extends RegistryBase<SearchServiceComponent> implements SearchServiceComponentRegistry {

	private final Map<String, SearchServiceComponent> aliasMap = new HashMap<String, SearchServiceComponent>();

	@Override
	protected void onElementAdded(final SearchServiceComponent object) {
		for (final String alias : object.getAliases()) {
			aliasMap.put(alias.toLowerCase(), object);
		}
	}

	@Override
	protected void onElementRemoved(final SearchServiceComponent object) {
		for (final String alias : object.getAliases()) {
			aliasMap.remove(alias.toLowerCase());
		}
	}

	@Override
	public SearchServiceComponent get(final String alias) {
		return aliasMap.get(alias.toLowerCase());
	}

}
