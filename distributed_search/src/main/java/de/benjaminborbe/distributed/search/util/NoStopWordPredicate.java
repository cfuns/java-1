package de.benjaminborbe.distributed.search.util;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;

public class NoStopWordPredicate implements Predicate<String> {

	private final Set<String> stopWords = new HashSet<String>();

	public NoStopWordPredicate() {
		stopWords.add("the");
		stopWords.add("der");
		stopWords.add("die");
		stopWords.add("das");
	}

	@Override
	public boolean apply(final String input) {
		return input != null && !stopWords.contains(input);
	}

}
