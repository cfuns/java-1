package de.benjaminborbe.tools.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class ComparatorChain<T> implements Comparator<T> {

	private final Collection<Comparator<T>> comparators;

	@SafeVarargs
	public ComparatorChain(final Comparator<T>... comparators) {
		this(Arrays.asList(comparators));
	}

	public ComparatorChain(final Collection<Comparator<T>> comparators) {
		this.comparators = comparators;
	}

	@Override
	public int compare(final T o1, final T o2) {
		final int result = compareIntern(o1, o2);
		// System.err.println(getClass().getSimpleName() + " compare " + o1 + " " + o2 +
		// " => " + result);
		return result;
	}

	private int compareIntern(final T o1, final T o2) {
		for (final Comparator<T> comparator : comparators) {
			final int result = comparator.compare(o1, o2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}
