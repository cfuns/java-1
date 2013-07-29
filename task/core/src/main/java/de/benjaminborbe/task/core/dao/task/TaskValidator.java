package de.benjaminborbe.task.core.dao.task;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskValidator extends ValidatorBase<TaskBean> {

	private final class ValidatorRuleDueStart implements ValidatorRule<TaskBean> {

		@Override
		public Collection<ValidationError> validate(final TaskBean bean) {
			final Set<ValidationError> result = new HashSet<ValidationError>();
			if (bean.getDue() != null && bean.getStart() != null && calendarUtil.isLT(bean.getDue(), bean.getStart())) {
				result.add(new ValidationErrorSimple("Due must be greater than start!"));
			}
			return result;
		}
	}

	private final CalendarUtil calendarUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public TaskValidator(final CalendarUtil calendarUtil, final ValidationConstraintValidator validationConstraintValidator) {
		this.calendarUtil = calendarUtil;
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<TaskBean> getType() {
		return TaskBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<TaskBean>> buildRules() {
		final Map<String, ValidatorRule<TaskBean>> result = new HashMap<String, ValidatorRule<TaskBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<TaskBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskBean bean) {
					final TaskIdentifier value = bean.getId();
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
			result.put(field, new ValidatorRule<TaskBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// due >= start
		{
			result.put("due", new ValidatorRuleDueStart());
			result.put("start", new ValidatorRuleDueStart());
		}

		// focus
		{
			final String field = "focus";
			result.put(field, new ValidatorRule<TaskBean>() {

				@Override
				public Collection<ValidationError> validate(final TaskBean bean) {
					final TaskFocus value = bean.getFocus();
					final List<ValidationConstraint<TaskFocus>> constraints = new ArrayList<ValidationConstraint<TaskFocus>>();
					constraints.add(new ValidationConstraintNotNull<TaskFocus>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
