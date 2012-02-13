package de.benjaminborbe.search.gui.service;

import de.benjaminborbe.search.api.SearchSpecial;

public interface SearchGuiSpecialSearchFactory {

	SearchSpecial findSpecial(String searchQuery);

}
