package de.benjaminborbe.task.core.dao.attachment;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAttachmentValidator extends ValidatorBase<TaskAttachmentBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public TaskAttachmentValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<TaskAttachmentBean> getType() {
		return TaskAttachmentBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<TaskAttachmentBean>> buildRules() {
		final Map<String, ValidatorRule<TaskAttachmentBean>> result = new HashMap<String, ValidatorRule<TaskAttachmentBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<TaskAttachmentBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskAttachmentBean bean) {
					final TaskAttachmentIdentifier value = bean.getId();
					final List<ValidationConstraint<TaskAttachmentIdentifier>> constraints = new ArrayList<ValidationConstraint<TaskAttachmentIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<TaskAttachmentIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<TaskAttachmentIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// task
		{
			final String field = "task";
			result.put(field, new ValidatorRule<TaskAttachmentBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskAttachmentBean bean) {
					final TaskIdentifier value = bean.getTask();
					final List<ValidationConstraint<TaskIdentifier>> constraints = new ArrayList<ValidationConstraint<TaskIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<TaskIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<TaskIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<TaskAttachmentBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskAttachmentBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
