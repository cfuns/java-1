package de.benjaminborbe.blog.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class BlogPostCreationException extends ValidationException {

	private static final long serialVersionUID = -9032566178935613356L;

	public BlogPostCreationException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
