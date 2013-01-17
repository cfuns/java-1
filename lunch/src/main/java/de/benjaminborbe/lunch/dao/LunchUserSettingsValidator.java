package de.benjaminborbe.lunch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

public class LunchUserSettingsValidator implements Validator<LunchUserSettingsBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public LunchUserSettingsValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<LunchUserSettingsBean> getType() {
		return LunchUserSettingsBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final LunchUserSettingsBean object) {
		final LunchUserSettingsBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		{
			final LunchUserSettingsIdentifier id = bean.getId();
			final List<ValidationConstraint<LunchUserSettingsIdentifier>> constraints = new ArrayList<ValidationConstraint<LunchUserSettingsIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<LunchUserSettingsIdentifier>());
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((LunchUserSettingsBean) object);
	}
}
