package de.benjaminborbe.search.core.service;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class SearchServiceSearchResultComparator extends ComparatorChain<SearchResult> {

	@Inject
	public SearchServiceSearchResultComparator(
		final SearchServiceSearchResultComparatorName searchServiceSearchResultComparatorName,
		final SearchServiceSearchResultComparatorPrio searchServiceSearchResultComparatorPrio,
		final SearchServiceSearchResultComparatorMatches searchServiceSearchResultComparatorMatches
	) {
		super(searchServiceSearchResultComparatorMatches, searchServiceSearchResultComparatorPrio, searchServiceSearchResultComparatorName);
	}
}
