package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class StringObjectMapperBoolean<T> extends StringObjectMapperBase<T, Boolean> {

	private final ParseUtil parseUtil;

	public StringObjectMapperBoolean(final String name, final ParseUtil parseUtil) {
		super(name);
		this.parseUtil = parseUtil;
	}

	@Override
	public String toString(final Boolean value) {
		return String.valueOf(value);
	}

	@Override
	public Boolean fromString(final String value) {
		try {
			return parseUtil.parseBoolean(value);
		}
		catch (final ParseException e) {
			return null;
		}
	}
}
