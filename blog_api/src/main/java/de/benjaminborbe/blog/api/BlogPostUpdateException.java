package de.benjaminborbe.blog.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class BlogPostUpdateException extends ValidationException {

	private static final long serialVersionUID = 1190553618713527945L;

	public BlogPostUpdateException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
