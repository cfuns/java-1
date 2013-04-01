package de.benjaminborbe.distributed.search.util;

import java.util.Collection;

public interface StopWords {

	void add(final String word);

	void remove(final String word);

	Collection<String> getWords();

	boolean contains(final String word);
}
