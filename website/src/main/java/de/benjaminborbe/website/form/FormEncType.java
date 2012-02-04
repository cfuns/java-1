package de.benjaminborbe.website.form;

public enum FormEncType {
	MULTIPART("multipart/form-data");

	private final String value;

	FormEncType(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}
}
