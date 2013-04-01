package de.benjaminborbe.search.dao;

import de.benjaminborbe.api.IdentifierBase;

public class SearchQueryHistoryIdentifier extends IdentifierBase<String> {

	public SearchQueryHistoryIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public SearchQueryHistoryIdentifier(final String id) {
		super(id);
	}

}
