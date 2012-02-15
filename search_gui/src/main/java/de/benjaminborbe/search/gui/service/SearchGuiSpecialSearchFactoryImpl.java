package de.benjaminborbe.search.gui.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.search.api.SearchSpecial;

@Singleton
public class SearchGuiSpecialSearchFactoryImpl implements SearchGuiSpecialSearchFactory {

	private final SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry;

	private final Logger logger;

	@Inject
	public SearchGuiSpecialSearchFactoryImpl(final Logger logger, final SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry) {
		this.logger = logger;
		this.searchGuiSpecialSearchRegistry = searchGuiSpecialSearchRegistry;
	}

	@Override
	public SearchSpecial findSpecial(final String searchQuery) {
		logger.trace("searchQuery: " + searchQuery);
		if (searchQuery != null && searchQuery.matches("^[a-zA-Z]+:.*?")) {
			final String name = searchQuery.substring(0, searchQuery.indexOf(":"));
			logger.trace("searchQuery request name: " + name);
			final Collection<SearchSpecial> list = searchGuiSpecialSearchRegistry.getAll();
			logger.trace("found " + list.size() + " special search");
			for (final SearchSpecial searchGuiSpecialSearch : list) {
				logger.trace("try " + searchGuiSpecialSearch.getName());
				if (name.equalsIgnoreCase(searchGuiSpecialSearch.getName())) {
					logger.trace("found special search");
					return searchGuiSpecialSearch;
				}
			}
		}
		return null;
	}
}
