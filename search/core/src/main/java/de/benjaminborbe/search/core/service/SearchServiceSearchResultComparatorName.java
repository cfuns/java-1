package de.benjaminborbe.search.core.service;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorBase;

public class SearchServiceSearchResultComparatorName extends ComparatorBase<SearchResult, String> {

	@Override
	public String getValue(final SearchResult o) {
		return o.getType() != null ? o.getType().toLowerCase() : null;
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
