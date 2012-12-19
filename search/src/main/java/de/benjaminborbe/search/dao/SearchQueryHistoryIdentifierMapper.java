package de.benjaminborbe.search.dao;

import de.benjaminborbe.tools.mapper.Mapper;

public class SearchQueryHistoryIdentifierMapper implements Mapper<SearchQueryHistoryIdentifier> {

	@Override
	public String toString(final SearchQueryHistoryIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public SearchQueryHistoryIdentifier fromString(final String value) {
		return value != null ? new SearchQueryHistoryIdentifier(value) : null;
	}

}
