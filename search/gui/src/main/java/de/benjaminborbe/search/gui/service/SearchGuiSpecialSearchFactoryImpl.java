package de.benjaminborbe.search.gui.service;

import java.util.Collection;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

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
		if (searchQuery != null) {
			final String searchQueryTrim = searchQuery.trim();
			if (searchQueryTrim.trim().length() > 0 && searchQueryTrim.matches("^[a-zA-Z]+:.*?")) {
				final String name = searchQueryTrim.substring(0, searchQueryTrim.indexOf(":"));
				logger.trace("searchTrim request name: " + name);
				final Collection<SearchSpecial> list = searchGuiSpecialSearchRegistry.getAll();
				logger.trace("found " + list.size() + " special search");
				for (final SearchSpecial searchGuiSpecialSearch : list) {
					for (final String searchName : searchGuiSpecialSearch.getAliases()) {
						logger.trace("try " + searchName);
						if (name.equalsIgnoreCase(searchName)) {
							logger.trace("found special search");
							return searchGuiSpecialSearch;
						}
					}
				}
			}
		}
		return null;
	}
}
