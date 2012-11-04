package de.benjaminborbe.tools.util;

import java.util.Comparator;

public abstract class ComparatorBase<E, T extends Comparable<T>> implements Comparator<E> {

	@Override
	public int compare(final E o1, final E o2) {
		final T arg0 = o1 != null ? getValue(o1) : null;
		final T arg1 = o2 != null ? getValue(o2) : null;
		if (arg0 != null && arg1 != null) {
			return arg0.compareTo(arg1);
		}
		else if (arg0 != null && arg1 == null) {
			return 1;
		}
		else if (arg0 == null && arg1 != null) {
			return -1;
		}
		return 0;
	}

	public abstract T getValue(E o);
}
