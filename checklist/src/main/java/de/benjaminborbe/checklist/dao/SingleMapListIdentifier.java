package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapListIdentifier<B> extends SingleMapBase<B, ChecklistListIdentifier> {

	public SingleMapListIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final ChecklistListIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public ChecklistListIdentifier fromString(final String value) {
		return value != null ? new ChecklistListIdentifier(value) : null;
	}

}
