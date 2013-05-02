package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorUtil {

	@Inject
	public ComparatorUtil() {
	}

	public <T> List<T> sort(final Collection<T> collection, final Comparator<T> comparator) {
		final List<T> list;
		if (collection instanceof List) {
			list = (List<T>) collection;
		} else {
			list = new ArrayList<>(collection);
		}
		Collections.sort(list, comparator);
		return list;
	}

	public <T extends Comparable> List<T> sort(final Collection<T> collection) {
		final List<T> list;
		if (collection instanceof List) {
			list = (List<T>) collection;
		} else {
			list = new ArrayList<>(collection);
		}
		Collections.sort(list);
		return list;
	}
}
