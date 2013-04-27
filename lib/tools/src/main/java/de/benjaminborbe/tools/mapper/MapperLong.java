package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public class MapperLong implements Mapper<Long> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperLong(final ParseUtil parseUtil) {
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
		} catch (final ParseException e) {
			return null;
		}
	}
}
