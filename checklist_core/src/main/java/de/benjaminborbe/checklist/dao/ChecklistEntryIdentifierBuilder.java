package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;

public class ChecklistEntryIdentifierBuilder implements IdentifierBuilder<String, ChecklistEntryIdentifier> {

	@Override
	public ChecklistEntryIdentifier buildIdentifier(final String value) {
		return new ChecklistEntryIdentifier(value);
	}

}
