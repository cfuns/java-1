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
		final SearchTermIterator searchTermIterator = new SearchTermIterator(searchQuery);
		for (final String word : searchTermIterator) {
			result.add(word);
		}
		return result;
	}

}
