package de.benjaminborbe.tools.json;

public class TestValue {

	private final String value;

	public TestValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value != null ? String.valueOf(value) : null;
	}

}
