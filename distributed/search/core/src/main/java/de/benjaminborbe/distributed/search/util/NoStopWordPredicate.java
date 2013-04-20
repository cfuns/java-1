package de.benjaminborbe.distributed.search.util;

import com.google.common.base.Predicate;
import javax.inject.Inject;

public class NoStopWordPredicate implements Predicate<String> {

	private final StopWords stopWords;

	@Inject
	public NoStopWordPredicate(final StopWords stopWords) {
		this.stopWords = stopWords;
	}

	@Override
	public boolean apply(final String input) {
		return input != null && !stopWords.contains(input);
	}

}
