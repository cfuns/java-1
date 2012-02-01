package de.benjaminborbe.search.gui.util;

import com.google.inject.Inject;

public class SearchGuiUtil {

	@Inject
	public SearchGuiUtil() {
	}

	public String[] buildSearchParts(final String searchQuery) {
		if (searchQuery == null) {
			return new String[0];
		}
		else {
			return searchQuery.toLowerCase().replaceAll("[^a-z0-9]", " ").split("\\s+");
		}
	}
}
