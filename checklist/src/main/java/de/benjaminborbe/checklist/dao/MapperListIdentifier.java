package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperListIdentifier implements Mapper<ChecklistListIdentifier> {

	@Override
	public String toString(final ChecklistListIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ChecklistListIdentifier fromString(final String value) {
		return value != null ? new ChecklistListIdentifier(value) : null;
	}

}
