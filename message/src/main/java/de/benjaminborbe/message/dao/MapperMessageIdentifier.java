package de.benjaminborbe.message.dao;

import de.benjaminborbe.tools.mapper.Mapper;

public class MapperMessageIdentifier implements Mapper<MessageIdentifier> {

	@Override
	public String toString(final MessageIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public MessageIdentifier fromString(final String value) {
		return value != null ? new MessageIdentifier(value) : null;
	}

}
