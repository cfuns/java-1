package de.benjaminborbe.task.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.validation.Validator;

public class TaskValidator implements Validator<TaskBean> {

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskValidator(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Class<TaskBean> getType() {
		return TaskBean.class;
	}

	public Collection<ValidationError> validate(final TaskBean object) {
		final TaskBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		{
			if (bean.getName() == null || bean.getName().length() == 0) {
				result.add(new ValidationErrorSimple("Name missing!"));
			}
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
