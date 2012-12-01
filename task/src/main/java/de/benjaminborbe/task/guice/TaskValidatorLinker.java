package de.benjaminborbe.task.guice;

import com.google.inject.Inject;

import de.benjaminborbe.task.dao.TaskContextValidator;
import de.benjaminborbe.task.dao.TaskValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class TaskValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final TaskValidator taskValidator, final TaskContextValidator taskContextValidator) {
		validatorRegistry.register(taskValidator);
		validatorRegistry.register(taskContextValidator);
	}
}
