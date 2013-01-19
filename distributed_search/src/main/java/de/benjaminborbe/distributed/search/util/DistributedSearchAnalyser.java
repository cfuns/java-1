package de.benjaminborbe.distributed.search.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;

import de.benjaminborbe.tools.search.SearchUtil;

public class DistributedSearchAnalyser {

	private final SearchUtil searchUtil;

	@Inject
	public DistributedSearchAnalyser(final SearchUtil searchUtil) {
		this.searchUtil = searchUtil;
	}

	public Collection<String> parseSearchTerm(final String content) {
		return filter(searchUtil.buildSearchParts(content));
	}

	@SuppressWarnings("unchecked")
	private Collection<String> filter(final Collection<String> list) {
		return Collections2.filter(list, Predicates.and(new MinWordLengthPredicate(), new MaxWordLengthPredicate(), new NoStopWordPredicate()));
	}

	public Map<String, Integer> parseWordRating(final String... contents) {
		final Map<String, Integer> result = new HashMap<String, Integer>();
		for (final String content : contents) {
			parseWordRating(content, 1, result);
		}
		return result;
	}

	public Map<String, Integer> parseWordRating(final String content, final int rating, final Map<String, Integer> result) {
		for (final String word : parseSearchTerm(content)) {
			final Integer m = result.get(word);
			if (m != null) {
				result.put(word, m + rating);
			}
			else {
				result.put(word, rating);
			}
		}
		return result;
	}
}
