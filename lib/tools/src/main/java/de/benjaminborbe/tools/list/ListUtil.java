package de.benjaminborbe.tools.list;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import javax.inject.Inject;

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

	public <T> List<T> randomize(final Collection<T> collection) {
		final List<T> result = Lists.newArrayList(collection);
		Collections.shuffle(result);
		return result;
	}
}
