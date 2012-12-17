package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperEntryIdentifier<B> extends StringObjectMapperBase<B, ChecklistEntryIdentifier> {

	public StringObjectMapperEntryIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final ChecklistEntryIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ChecklistEntryIdentifier fromString(final String value) {
		return value != null ? new ChecklistEntryIdentifier(value) : null;
	}

}
