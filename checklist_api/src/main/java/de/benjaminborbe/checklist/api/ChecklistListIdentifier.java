package de.benjaminborbe.checklist.api;

import de.benjaminborbe.api.IdentifierBase;

public class ChecklistListIdentifier extends IdentifierBase<String> {

	public ChecklistListIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public ChecklistListIdentifier(final String id) {
		super(id);
	}

}
