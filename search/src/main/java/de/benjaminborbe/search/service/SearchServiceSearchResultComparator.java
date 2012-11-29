package de.benjaminborbe.search.service;

import com.google.inject.Inject;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorChain;

public class SearchServiceSearchResultComparator extends ComparatorChain<SearchResult> {

	@SuppressWarnings("unchecked")
	@Inject
	public SearchServiceSearchResultComparator(final SearchServiceSearchResultComparatorName a, final SearchServiceSearchResultComparatorPrio b) {
		super(b, a);
	}
}
