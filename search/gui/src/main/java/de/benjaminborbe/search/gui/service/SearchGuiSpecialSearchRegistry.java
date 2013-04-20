package de.benjaminborbe.search.gui.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class SearchGuiSpecialSearchRegistry extends RegistryBase<SearchSpecial> {

	@Inject
	public SearchGuiSpecialSearchRegistry(final SearchGuiBugzillaSpecialSearch searchGuiBugzillaSpecialSearch) {
		super(searchGuiBugzillaSpecialSearch);
	}

}
