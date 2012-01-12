package de.benjaminborbe.search.util;

import com.google.inject.Inject;

public class SearchUtil {

	@Inject
	public SearchUtil() {
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
