package de.benjaminborbe.checklist.api;

import de.benjaminborbe.api.IdentifierBase;

public class ChecklistEntryIdentifier extends IdentifierBase<String> {

	public ChecklistEntryIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public ChecklistEntryIdentifier(final String id) {
		super(id);
	}

}
