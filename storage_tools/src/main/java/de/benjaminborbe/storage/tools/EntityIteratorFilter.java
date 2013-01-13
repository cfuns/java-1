package de.benjaminborbe.storage.tools;

import com.google.common.base.Predicate;

import de.benjaminborbe.api.IteratorBase;
import de.benjaminborbe.tools.iterator.IteratorFilter;

public class EntityIteratorFilter<T> extends IteratorFilter<T, EntityIteratorException> implements EntityIterator<T> {

	public EntityIteratorFilter(final IteratorBase<T, EntityIteratorException> entityIterator, final Predicate<T> predicate) {
		super(entityIterator, predicate);
	}

}
