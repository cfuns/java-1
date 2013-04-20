package de.benjaminborbe.tools.mapper;

import javax.inject.Inject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MapperInteger implements Mapper<Integer> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperInteger(final ParseUtil parseUtil) {
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
