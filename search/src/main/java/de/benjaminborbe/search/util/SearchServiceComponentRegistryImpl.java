package de.benjaminborbe.search.util;

import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.util.RegistryImpl;

@Singleton
public class SearchServiceComponentRegistryImpl extends RegistryImpl<SearchServiceComponent> implements
		SearchServiceComponentRegistry {

}
