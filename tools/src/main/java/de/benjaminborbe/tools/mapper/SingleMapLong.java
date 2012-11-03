package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class SingleMapLong<T> extends SingleMapBase<T, Long> {

	private final ParseUtil parseUtil;

	public SingleMapLong(final String name, final ParseUtil parseUtil) {
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
