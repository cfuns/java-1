package de.benjaminborbe.task.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;

public class TaskCreationException extends ValidationException {

	private static final long serialVersionUID = -9032566178935613356L;

	public TaskCreationException(final ValidationResult validationResult) {
		super(validationResult);
	}

}
