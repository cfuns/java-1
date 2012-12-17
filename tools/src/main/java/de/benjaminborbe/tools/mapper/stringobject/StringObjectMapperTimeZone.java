package de.benjaminborbe.tools.mapper.stringobject;

import java.util.TimeZone;

public class StringObjectMapperTimeZone<T> extends StringObjectMapperBase<T, TimeZone> {

	public StringObjectMapperTimeZone(final String name) {
		super(name);
	}

	@Override
	public String toString(final TimeZone value) {
		return value != null ? value.getID() : null;
	}

	@Override
	public TimeZone fromString(final String value) {
		return value != null ? TimeZone.getTimeZone(value) : null;
	}
}
