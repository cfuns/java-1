package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public class MapperBoolean implements Mapper<Boolean> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperBoolean(final ParseUtil parseUtil) {
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
		} catch (final ParseException e) {
			return null;
		}
	}
}
