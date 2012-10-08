package de.benjaminborbe.search.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.benjaminborbe.search.api.SearchServiceComponent;

public class SearchServiceComponentComparator implements Comparator<SearchServiceComponent> {

	private final Map<String, Integer> prio = new HashMap<String, Integer>();

	public SearchServiceComponentComparator() {
		add("URL", 1);
		add("GOOGLE", 2);
	}

	private void add(final String string, final int i) {
		prio.put(string.toLowerCase(), i);
	}

	@Override
	public int compare(final SearchServiceComponent o1, final SearchServiceComponent o2) {
		final Integer prio1 = prio.get(o1.getName().toLowerCase());
		final Integer prio2 = prio.get(o2.getName().toLowerCase());
		if (prio1 != null && prio2 != null) {
			final int result = prio1.compareTo(prio2);
			if (result != 0) {
				return result;
			}
			else {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		}
		else if (prio1 != null && prio2 == null) {
			return -1;
		}
		else if (prio1 == null && prio2 != null) {
			return 1;
		}
		else {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	}
}
