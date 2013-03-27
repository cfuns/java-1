package de.benjaminborbe.tools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

public class ComparatorUtil {

	@Inject
	public ComparatorUtil() {
	}

	public <T> List<T> sort(final Collection<T> collection, final Comparator<T> comparator) {
		List<T> list;
		if (collection instanceof List) {
			list = (List<T>) collection;
		}
		else {
			list = new ArrayList<T>(collection);
		}
		Collections.sort(list, comparator);
		return list;
	}
}
