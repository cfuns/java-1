package de.benjaminborbe.dhl.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.dhl.api.DhlIdentifier;

public class DhlIdentifierBuilder implements IdentifierBuilder<String, DhlIdentifier> {

	@Override
	public DhlIdentifier buildIdentifier(final String value) {
		return new DhlIdentifier(value);
	}

}
