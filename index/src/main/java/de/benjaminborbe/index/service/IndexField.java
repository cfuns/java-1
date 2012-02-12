package de.benjaminborbe.index.service;

public enum IndexField {
	ID("id"),
	TITLE("title"),
	URL("url"),
	CONTENT("content");

	private final String fieldName;

	IndexField(final String fieldName) {
		this.fieldName = fieldName;

	}

	public String getFieldName() {
		return fieldName;
	}
}
