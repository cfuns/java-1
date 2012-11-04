package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class SingleMapInteger<T> extends SingleMapBase<T, Integer> {

	private final ParseUtil parseUtil;

	public SingleMapInteger(final String name, final ParseUtil parseUtil) {
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
