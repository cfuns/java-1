package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.IdentifierBuilder;

public class TestIdentifierBuilder implements IdentifierBuilder<String, TestIdentifier> {

	@Override
	public TestIdentifier buildIdentifier(final String value) {
		return new TestIdentifier(value);
	}

}
