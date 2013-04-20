package de.benjaminborbe.tools.search;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class SearchUtil {

	@Inject
	public SearchUtil() {
	}

	public List<String> buildSearchParts(final String searchQuery) {
		final List<String> result = new ArrayList<>();
		for (final String word : buildSearchTermIterator(searchQuery)) {
			result.add(word);
		}
		return result;
	}

	private Iterable<String> buildSearchTermIterator(final String searchQuery) {
		final SearchTermIterator searchTermIterator = new SearchTermIterator(searchQuery);
		final SearchTermIteratorCamelCase searchTermIteratorCamelCase = new SearchTermIteratorCamelCase(searchTermIterator);
		final SearchTermIteratorLowerCase searchTermIteratorLowerCase = new SearchTermIteratorLowerCase(searchTermIteratorCamelCase);
		return searchTermIteratorLowerCase;
	}

}
