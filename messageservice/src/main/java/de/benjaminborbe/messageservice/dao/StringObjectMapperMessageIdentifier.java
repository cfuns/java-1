package de.benjaminborbe.messageservice.dao;

import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperMessageIdentifier<B> extends StringObjectMapperBase<B, MessageIdentifier> {

	public StringObjectMapperMessageIdentifier(final String name) {
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
