package de.benjaminborbe.distributed.search.dao;

import de.benjaminborbe.tools.mapper.Mapper;

public class MapperDistributedSearchPageIdentifier implements Mapper<DistributedSearchPageIdentifier> {

	@Override
	public String toString(final DistributedSearchPageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public DistributedSearchPageIdentifier fromString(final String value) {
		return value != null ? new DistributedSearchPageIdentifier(value) : null;
	}

}
