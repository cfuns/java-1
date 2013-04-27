package de.benjaminborbe.tools.util;

import java.util.Comparator;

public abstract class ComparatorBase<E, T extends Comparable<T>> implements Comparator<E> {

	public boolean inverted() {
		return false;
	}

	public boolean nullFirst() {
		return true;
	}

	@Override
	public int compare(final E o1, final E o2) {
		final T arg0 = o1 != null ? getValue(o1) : null;
		final T arg1 = o2 != null ? getValue(o2) : null;
		final int result = compareIntern(arg0, arg1);
		// System.err.println(getClass().getSimpleName() + " compare " + arg0 + " " + arg1 +
		// " => " + result);
		return result;
	}

	private int compareIntern(final T arg0, final T arg1) {

		if (arg0 != null && arg1 != null) {
			if (inverted()) {
				return arg1.compareTo(arg0);
			} else {
				return arg0.compareTo(arg1);
			}
		} else if (arg0 != null && arg1 == null) {
			if (nullFirst()) {
				return 1;
			} else {
				return -1;
			}
		} else if (arg0 == null && arg1 != null) {
			if (nullFirst()) {
				return -1;
			} else {
				return 1;
			}
		}
		return 0;
	}

	public abstract T getValue(E o);

}
