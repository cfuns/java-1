package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class StringObjectMapperInteger<T> extends StringObjectMapperBase<T, Integer> {

	private final ParseUtil parseUtil;

	public StringObjectMapperInteger(final String name, final ParseUtil parseUtil) {
		super(name);
		this.parseUtil = parseUtil;
	}

	@Override
	public String toString(final Integer value) {
		return String.valueOf(value);
	}

	@Override
	public Integer fromString(final String value) {
		try {
			return parseUtil.parseInt(value);
		}
		catch (final ParseException e) {
			return null;
		}
	}
}
