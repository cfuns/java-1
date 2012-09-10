package de.benjaminborbe.blog.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class BlogPostDeleteException extends ValidationException {

	private static final long serialVersionUID = -2563460013138480784L;

	public BlogPostDeleteException(final ValidationResult validationResult) {
		super(validationResult);
	}
}
