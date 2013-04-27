package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public class MapperDouble implements Mapper<Double> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperDouble(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public String toString(final Double value) {
		return String.valueOf(value);
	}

	@Override
	public Double fromString(final String value) {
		try {
			return parseUtil.parseDouble(value);
		} catch (final ParseException e) {
			return null;
		}
	}
}
