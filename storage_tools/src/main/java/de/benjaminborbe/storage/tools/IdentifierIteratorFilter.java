package de.benjaminborbe.storage.tools;

import com.google.common.base.Predicate;

import de.benjaminborbe.api.IteratorBase;
import de.benjaminborbe.tools.iterator.IteratorFilter;

public class IdentifierIteratorFilter<T> extends IteratorFilter<T, IdentifierIteratorException> implements IdentifierIterator<T> {

	public IdentifierIteratorFilter(final IteratorBase<T, IdentifierIteratorException> entityIterator, final Predicate<T> predicate) {
		super(entityIterator, predicate);
	}
}
