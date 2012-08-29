package de.benjaminborbe.bookmark.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class BookmarkCreationException extends ValidationException {

	private static final long serialVersionUID = -9032566178935613356L;

	public BookmarkCreationException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
