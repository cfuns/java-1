package de.benjaminborbe.index.service;

public enum IndexField {
	TITLE("title"),
	URL("url"),
	CONTENT("");

	private final String fieldName;

	IndexField(final String fieldName) {
		this.fieldName = fieldName;

	}

	public String getFieldName() {
		return fieldName;
	}
}