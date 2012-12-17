package de.benjaminborbe.tools.mapper.stringobject;

public class StringObjectMapperString<T> extends StringObjectMapperBase<T, String> {

	public StringObjectMapperString(final String name) {
		super(name);
	}

	@Override
	public String toString(final String value) {
		return value;
	}

	@Override
	public String fromString(final String value) {
		return value;
	}

}
