package de.benjaminborbe.task.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.tools.validation.Validator;

public class TaskContextValidator implements Validator<TaskContextBean> {

	@Override
	public Class<TaskContextBean> getType() {
		return TaskContextBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final Object object) {
		final TaskContextBean bean = (TaskContextBean) object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		final String name = bean.getName();
		{
			if (name == null || name.length() == 0) {
				result.add(new ValidationErrorSimple("name missing"));
			}
		}

		return result;
	}
}
