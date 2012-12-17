package de.benjaminborbe.messageservice.dao;

import de.benjaminborbe.api.IdentifierBuilder;

public class MessageIdentifierBuilder implements IdentifierBuilder<String, MessageIdentifier> {

	@Override
	public MessageIdentifier buildIdentifier(final String value) {
		return new MessageIdentifier(value);
	}

}
