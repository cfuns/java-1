package de.benjaminborbe.message.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.message.api.MessageIdentifier;

public class MessageIdentifierBuilder implements IdentifierBuilder<String, MessageIdentifier> {

	@Override
	public MessageIdentifier buildIdentifier(final String value) {
		return new MessageIdentifier(value);
	}

}
