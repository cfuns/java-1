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
		final T arg0;
		final T arg1;
		if (inverted()) {
			arg0 = o2 != null ? getValue(o2) : null;
			arg1 = o1 != null ? getValue(o1) : null;
		}
		else {
			arg0 = o1 != null ? getValue(o1) : null;
			arg1 = o2 != null ? getValue(o2) : null;
		}

		if (arg0 != null && arg1 != null) {
			return arg0.compareTo(arg1);
		}
		else if (arg0 != null && arg1 == null) {
			if (nullFirst()) {
				return 1;
			}
			else {
				return -1;
			}
		}
		else if (arg0 == null && arg1 != null) {
			if (nullFirst()) {
				return -1;
			}
			else {
				return 1;
			}
		}
		return 0;
	}

	public abstract T getValue(E o);

}
