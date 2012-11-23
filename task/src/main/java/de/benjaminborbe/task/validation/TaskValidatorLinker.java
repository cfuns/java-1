package de.benjaminborbe.task.validation;

import com.google.inject.Inject;

import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class TaskValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final TaskValidator validator) {
		validatorRegistry.register(validator);
	}
}
