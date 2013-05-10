package de.benjaminborbe.task.core.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.task.core.dao.attachment.TaskAttachmentValidator;
import de.benjaminborbe.task.core.dao.context.TaskContextValidator;
import de.benjaminborbe.task.core.dao.task.TaskValidator;

import javax.inject.Inject;

public class TaskValidatorLinker {

	@Inject
	public static void link(
		final ValidatorRegistry validatorRegistry,
		final TaskValidator taskValidator,
		final TaskContextValidator taskContextValidator,
		final TaskAttachmentValidator taskAttachmentValidator
	) {
		validatorRegistry.register(taskValidator);
		validatorRegistry.register(taskContextValidator);
		validatorRegistry.register(taskAttachmentValidator);
	}
}
