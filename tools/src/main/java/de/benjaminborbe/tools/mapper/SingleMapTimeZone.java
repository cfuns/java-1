package de.benjaminborbe.tools.mapper;

import java.util.TimeZone;

public class SingleMapTimeZone<T> extends SingleMapBase<T, TimeZone> {

	public SingleMapTimeZone(final String name) {
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
