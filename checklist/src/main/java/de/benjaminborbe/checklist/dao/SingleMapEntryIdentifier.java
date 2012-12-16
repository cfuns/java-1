package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapEntryIdentifier<B> extends SingleMapBase<B, ChecklistEntryIdentifier> {

	public SingleMapEntryIdentifier(final String name) {
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
