package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class ChecklistListValidator implements Validator<ChecklistListBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public ChecklistListValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<ChecklistListBean> getType() {
		return ChecklistListBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final ChecklistListBean object) {
		final ChecklistListBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();
		{
			final String name = bean.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}
		{
			final ChecklistListIdentifier id = bean.getId();
			final List<ValidationConstraint<ChecklistListIdentifier>> constraints = new ArrayList<ValidationConstraint<ChecklistListIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<ChecklistListIdentifier>());
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}
		{
			final UserIdentifier owner = bean.getOwner();
			final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<ValidationConstraint<UserIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
			result.addAll(validationConstraintValidator.validate("owner", owner, constraints));
		}
		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((ChecklistListBean) object);
	}
}
