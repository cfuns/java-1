package de.benjaminborbe.distributed.search.dao;

import de.benjaminborbe.api.IdentifierBuilder;

public class DistributedSearchPageIdentifierBuilder implements IdentifierBuilder<String, DistributedSearchPageIdentifier> {

	@Override
	public DistributedSearchPageIdentifier buildIdentifier(final String value) {
		return new DistributedSearchPageIdentifier(value);
	}

}
