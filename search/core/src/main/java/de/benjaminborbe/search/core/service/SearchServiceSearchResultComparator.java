package de.benjaminborbe.search.core.service;

import javax.inject.Inject;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorChain;

public class SearchServiceSearchResultComparator extends ComparatorChain<SearchResult> {

	@Inject
	public SearchServiceSearchResultComparator(
		final SearchServiceSearchResultComparatorName searchServiceSearchResultComparatorName,
		final SearchServiceSearchResultComparatorPrio searchServiceSearchResultComparatorPrio,
		final SearchServiceSearchResultComparatorMatches searchServiceSearchResultComparatorMatches) {
		super(searchServiceSearchResultComparatorMatches, searchServiceSearchResultComparatorPrio, searchServiceSearchResultComparatorName);
	}
}
