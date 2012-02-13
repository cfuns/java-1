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
		logger.debug("searchQuery: " + searchQuery);
		if (searchQuery != null && searchQuery.matches("^[a-zA-Z]+:.*?")) {
			final String name = searchQuery.substring(0, searchQuery.indexOf(":"));
			logger.debug("searchQuery request name: " + name);
			final Collection<SearchSpecial> list = searchGuiSpecialSearchRegistry.getAll();
			logger.debug("found " + list.size() + " special search");
			for (final SearchSpecial searchGuiSpecialSearch : list) {
				logger.debug("try " + searchGuiSpecialSearch.getName());
				if (name.equalsIgnoreCase(searchGuiSpecialSearch.getName())) {
					logger.debug("found special search");
					return searchGuiSpecialSearch;
				}
			}
		}
		return null;
	}
}
