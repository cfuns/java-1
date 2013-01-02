package de.benjaminborbe.distributed.search.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;

public class DistributedSearchAnalyser {

	@Inject
	public DistributedSearchAnalyser() {
	}

	public Collection<String> parseSearchTerm(final String content) {
		final String[] parts = content.toLowerCase().replaceAll("[^a-z]", " ").split("\\s+");
		return filter(Arrays.asList(parts));
	}

	@SuppressWarnings("unchecked")
	private Collection<String> filter(final Collection<String> list) {
		return Collections2.filter(list, Predicates.and(new MinWordLengthPredicate(), new MaxWordLengthPredicate(), new NoStopWordPredicate()));
	}

	public Map<String, Integer> parseWordRating(final String content) {
		final Map<String, Integer> result = new HashMap<String, Integer>();
		for (final String word : parseSearchTerm(content)) {
			final Integer m = result.get(word);
			if (m != null) {
				result.put(word, m + 1);
			}
			else {
				result.put(word, 1);
			}
		}
		return result;
	}
}
