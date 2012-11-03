package de.benjaminborbe.tools.mapper;

public class SingleMapString<T> extends SingleMapBase<T, String> {

	public SingleMapString(final String name) {
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
