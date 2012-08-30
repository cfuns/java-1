package de.benjaminborbe.wiki.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class WikiPageCreateException extends ValidationException {

	private static final long serialVersionUID = -2127052496605960855L;

	public WikiPageCreateException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
