package de.benjaminborbe.poker.util;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCollectionBase;

public class MapperUserIdentifierCollection extends MapperCollectionBase<UserIdentifier> {

	@Inject
	public MapperUserIdentifierCollection(final MapperUserIdentifier mapper) {
		super(mapper);
	}

}
