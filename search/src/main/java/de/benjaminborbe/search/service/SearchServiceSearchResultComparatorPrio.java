package de.benjaminborbe.search.service;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorBase;

public class SearchServiceSearchResultComparatorPrio extends ComparatorBase<SearchResult, Integer> {

	private final Map<String, Integer> prio = new HashMap<String, Integer>();

	@Inject
	public SearchServiceSearchResultComparatorPrio() {
		int prio = 0;
		add("URL", prio++);
		add("GOOGLE", prio++);
		add("CONFLUENCE", prio++);
		add("BOOKMARK", prio++);
		add("WEB", prio++);
	}

	private void add(final String string, final int i) {
		prio.put(string.toLowerCase(), i);
	}

	@Override
	public Integer getValue(final SearchResult o) {
		return prio.get(o.getType().toLowerCase());
	}

	@Override
	public boolean inverted() {
		return true;
	}

	@Override
	public boolean nullFirst() {
		return false;
	}

}
