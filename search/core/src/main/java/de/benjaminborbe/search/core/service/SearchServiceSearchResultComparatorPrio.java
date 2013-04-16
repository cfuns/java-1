package de.benjaminborbe.search.core.service;

import com.google.inject.Inject;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorBase;

import java.util.HashMap;
import java.util.Map;

public class SearchServiceSearchResultComparatorPrio extends ComparatorBase<SearchResult, Integer> {

	private final Map<String, Integer> prio = new HashMap<>();

	@Inject
	public SearchServiceSearchResultComparatorPrio() {
		int prio = 0;
		add("URL", ++prio);
		add("TASK", ++prio);
		add("GOOGLE", ++prio);
		add("CONFLUENCE", ++prio);
		add("BOOKMARK", ++prio);
		add("WEB", ++prio);
	}

	private void add(final String string, final int i) {
		prio.put(string.toLowerCase(), i);
	}

	@Override
	public Integer getValue(final SearchResult o) {
		return o.getType() != null ? prio.get(o.getType().toLowerCase()) : null;
	}

	@Override
	public boolean inverted() {
		return false;
	}

	@Override
	public boolean nullFirst() {
		return false;
	}

}
