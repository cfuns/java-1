package de.benjaminborbe.wiki.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class WikiSpaceCreateException extends ValidationException {

	private static final long serialVersionUID = 2970525022110775500L;

	public WikiSpaceCreateException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
