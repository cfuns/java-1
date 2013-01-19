package de.benjaminborbe.tools.search;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

public class SearchUtil {

	@Inject
	public SearchUtil() {
	}

	public List<String> buildSearchParts(final String searchQuery) {
		final List<String> result = new ArrayList<String>();
		if (searchQuery != null) {
			for (final String part : searchQuery.toLowerCase().replaceAll("[^a-z0-9öäüß]", " ").split("\\s+")) {
				final String partTrim = part.trim();
				if (partTrim.length() > 0) {
					result.add(partTrim);
				}
			}
		}
		return result;
	}
}
