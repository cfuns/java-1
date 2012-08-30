package de.benjaminborbe.dhl.api;

import de.benjaminborbe.api.IdentifierBase;

public class DhlIdentifier extends IdentifierBase<String> {

	public DhlIdentifier(final String id) {
		super(id);
	}

	public DhlIdentifier(final long id) {
		super(String.valueOf(id));
	}

}
