package de.benjaminborbe.lucene.index.service;

public enum LuceneIndexField {
	ID("id"),
	TITLE("title"),
	URL("url"),
	CONTENT("content");

	private final String fieldName;

	private LuceneIndexField(final String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
