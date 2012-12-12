package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringOnlyLetters;

public class TaskValidator implements Validator<TaskBean> {

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
	public Collection<ValidationError> validate(final TaskBean object) {
		final TaskBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		{
			final String name = bean.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringOnlyLetters());
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}

		// due >= start
		{
			if (bean.getDue() != null && bean.getStart() != null && calendarUtil.isLT(bean.getDue(), bean.getStart())) {
				result.add(new ValidationErrorSimple("Due must be greater than start!"));
			}
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((TaskBean) object);
	}
}
