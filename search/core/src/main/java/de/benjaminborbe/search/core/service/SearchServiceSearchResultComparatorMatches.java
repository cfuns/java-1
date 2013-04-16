package de.benjaminborbe.search.core.service;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorBase;

public class SearchServiceSearchResultComparatorMatches extends ComparatorBase<SearchResult, Integer> {

	@Override
	public boolean inverted() {
		return true;
	}

	@Override
	public boolean nullFirst() {
		return false;
	}

	@Override
	public Integer getValue(final SearchResult o) {
		return o.getMatchCounter();
	}

}
