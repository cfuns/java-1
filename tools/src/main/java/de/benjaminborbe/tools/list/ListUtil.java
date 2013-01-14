package de.benjaminborbe.tools.list;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class ListUtil {

	@Inject
	public ListUtil() {
	}

	public <T> List<T> toList(final Collection<T> collection) {
		if (collection instanceof List) {
			return (List<T>) collection;
		}
		else {
			return Lists.newArrayList(collection);
		}
	}
}
