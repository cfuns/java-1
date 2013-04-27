package de.benjaminborbe.distributed.search.util;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StopWordsImpl implements StopWords {

	private final Set<String> words = new HashSet<>();

	private final Logger logger;

	@Inject
	public StopWordsImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void add(final String word) {
		logger.trace("add stopword: " + word);
		words.add(word);
	}

	@Override
	public void remove(final String word) {
		logger.trace("remove stopword: " + word);
		words.remove(word);
	}

	@Override
	public Collection<String> getWords() {
		logger.trace("getWords");
		return words;
	}

	@Override
	public boolean contains(final String word) {
		final boolean result = words.contains(word);
		logger.trace("contains stopword: " + word + " => " + result);
		return result;
	}
}
