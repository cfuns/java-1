package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class StringObjectMapperLong<T> extends StringObjectMapperBase<T, Long> {

	private final ParseUtil parseUtil;

	public StringObjectMapperLong(final String name, final ParseUtil parseUtil) {
		super(name);
		this.parseUtil = parseUtil;
	}

	@Override
	public String toString(final Long value) {
		return String.valueOf(value);
	}

	@Override
	public Long fromString(final String value) {
		try {
			return parseUtil.parseLong(value);
		}
		catch (final ParseException e) {
			return null;
		}
	}
}
