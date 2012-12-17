package de.benjaminborbe.messageservice.dao;

import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapMessageIdentifier<B> extends SingleMapBase<B, MessageIdentifier> {

	public SingleMapMessageIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final MessageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public MessageIdentifier fromString(final String value) {
		return value != null ? new MessageIdentifier(value) : null;
	}

}
