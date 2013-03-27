package de.benjaminborbe.tools.mapper;

public class MapperString implements Mapper<String> {

	@Override
	public String fromString(final String value) {
		return value;
	}

	@Override
	public String toString(final String value) {
		return value;
	}

}
