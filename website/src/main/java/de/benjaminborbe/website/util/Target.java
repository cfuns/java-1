package de.benjaminborbe.website.util;

public enum Target {

	BLANK("_blank"),
	SELF("_self"),
	PARENT("_parent"),
	TOP("_top");

	private final String value;

	Target(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
