package de.benjaminborbe.checklist.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;

public class ChecklistListIdentifierBuilder implements IdentifierBuilder<String, ChecklistListIdentifier> {

	@Override
	public ChecklistListIdentifier buildIdentifier(final String value) {
		return new ChecklistListIdentifier(value);
	}

}
