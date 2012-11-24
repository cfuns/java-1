package de.benjaminborbe.gallery.util;

import com.google.common.base.Predicate;

public class SharedPredicate<E extends HasShared> implements Predicate<E> {

	@Override
	public boolean apply(final HasShared hasShared) {
		return hasShared != null && Boolean.TRUE.equals(hasShared.getShared());
	}
}
