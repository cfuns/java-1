package de.benjaminborbe.search.gui.service;

import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SearchGuiSpecialSearchRegistry extends RegistryBase<SearchSpecial> {

	@Inject
	public SearchGuiSpecialSearchRegistry(
		final SearchGuiBugzillaSpecialSearch searchGuiBugzillaSpecialSearch,
		final SearchGuiGithubSpecialSearch searchGuiGithubSpecialSearch
	) {
		super(searchGuiBugzillaSpecialSearch, searchGuiGithubSpecialSearch);
	}

}
