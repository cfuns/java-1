package de.benjaminborbe.task.core.dao.context;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringNot;
import de.benjaminborbe.task.api.TaskContextIdentifier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskContextValidator extends ValidatorBase<TaskContextBean> {

	private final class ValidationConstrainAllowedCharacters implements ValidationConstraint<String> {

		@Override
		public boolean precondition(final String object) {
			return object != null;
		}

		@Override
		public boolean validate(final String object) {
			for (final char c : object.toCharArray()) {
				if (!Character.isLetter(c) && c != '-') {
					return false;
				}
			}
			return true;
		}
	}

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public TaskContextValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<TaskContextBean> getType() {
		return TaskContextBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<TaskContextBean>> buildRules() {
		final Map<String, ValidatorRule<TaskContextBean>> result = new HashMap<String, ValidatorRule<TaskContextBean>
			>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<TaskContextBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskContextBean bean) {
					final TaskContextIdentifier value = bean.getId();
					final List<ValidationConstraint<TaskContextIdentifier>> constraints = new ArrayList<ValidationConstraint<TaskContextIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<TaskContextIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<TaskContextIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<TaskContextBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskContextBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstrainAllowedCharacters());
					constraints.add(new ValidationConstraintStringNot("all", "none"));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
