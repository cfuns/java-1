package de.benjaminborbe.search.util;

import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.util.RegistryBase;

@Singleton
public class SearchServiceComponentRegistryImpl extends RegistryBase<SearchServiceComponent> implements SearchServiceComponentRegistry {

}
