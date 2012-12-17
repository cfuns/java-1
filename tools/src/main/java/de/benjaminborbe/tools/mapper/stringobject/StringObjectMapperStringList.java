package de.benjaminborbe.tools.mapper.stringobject;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringObjectMapperStringList<T> extends StringObjectMapperBase<T, List<String>> {

	public StringObjectMapperStringList(final String name) {
		super(name);
	}

	@Override
	public String toString(final List<String> value) {
		if (value == null) {
			return null;
		}
		return StringUtils.join(value, ",");
	}

	@Override
	public List<String> fromString(final String value) {
		if (value == null) {
			return null;
		}
		return Arrays.asList(value.split(","));
	}
}
