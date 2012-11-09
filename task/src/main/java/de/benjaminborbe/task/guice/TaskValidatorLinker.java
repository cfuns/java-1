package de.benjaminborbe.task.guice;

import com.google.inject.Inject;

import de.benjaminborbe.task.validation.TaskValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class TaskValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final TaskValidator bookmarkValidator) {
		validatorRegistry.register(bookmarkValidator);
	}
}
