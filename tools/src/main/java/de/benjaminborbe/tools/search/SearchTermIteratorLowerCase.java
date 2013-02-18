package de.benjaminborbe.tools.search;

import de.benjaminborbe.api.IteratorWithoutException;
import de.benjaminborbe.tools.iterator.IteratorWithoutExceptionBase;

public class SearchTermIteratorLowerCase extends IteratorWithoutExceptionBase<String> {

	private final IteratorWithoutException<String> iterator;

	public SearchTermIteratorLowerCase(final IteratorWithoutException<String> iterator) {
		this.iterator = iterator;
	}

	@Override
	protected String buildNext() {
		if (iterator.hasNext()) {
			return iterator.next().toLowerCase();
		}
		return null;
	}
}
