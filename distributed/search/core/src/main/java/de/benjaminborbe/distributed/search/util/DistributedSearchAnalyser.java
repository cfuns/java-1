package de.benjaminborbe.distributed.search.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import javax.inject.Inject;
import de.benjaminborbe.tools.search.SearchUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DistributedSearchAnalyser {

	private final Logger logger;

	private final SearchUtil searchUtil;

	private final StopWords stopWords;

	@Inject
	public DistributedSearchAnalyser(final Logger logger, final SearchUtil searchUtil, final StopWords stopWords) {
		this.logger = logger;
		this.searchUtil = searchUtil;
		this.stopWords = stopWords;
	}

	public Collection<String> parseSearchTerm(final String content) {
		logger.debug("parseSearchTerm: " + content);
		return filter(searchUtil.buildSearchParts(content));
	}

	@SuppressWarnings("unchecked")
	private Collection<String> filter(final Collection<String> list) {
		return Collections2.filter(list, Predicates.and(new MinWordLengthPredicate(), new MaxWordLengthPredicate(), new NoStopWordPredicate(stopWords)));
	}

	public Map<String, Integer> parseWordRating(final String... contents) {
		final Map<String, Integer> result = new HashMap<>();
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
			} else {
				result.put(word, rating);
			}
		}
		return result;
	}
}
