package de.benjaminborbe.poker.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCollectionBase;

import javax.inject.Inject;

public class MapperUserIdentifierCollection extends MapperCollectionBase<UserIdentifier> {

	@Inject
	public MapperUserIdentifierCollection(final MapperUserIdentifier mapper) {
		super(mapper);
	}

}
