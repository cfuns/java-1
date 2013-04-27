package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

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
		} catch (final ParseException e) {
			return null;
		}
	}
}
