package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringOnlyLetters;

public class ChecklistEntryValidator implements Validator<ChecklistEntryBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public ChecklistEntryValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<ChecklistEntryBean> getType() {
		return ChecklistEntryBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final ChecklistEntryBean object) {
		final ChecklistEntryBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();
		{
			final String name = bean.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringOnlyLetters());
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}
		{
			final ChecklistEntryIdentifier id = bean.getId();
			final List<ValidationConstraint<ChecklistEntryIdentifier>> constraints = new ArrayList<ValidationConstraint<ChecklistEntryIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<ChecklistEntryIdentifier>());
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}
		{
			final ChecklistListIdentifier listId = bean.getListId();
			final List<ValidationConstraint<ChecklistListIdentifier>> constraints = new ArrayList<ValidationConstraint<ChecklistListIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<ChecklistListIdentifier>());
			result.addAll(validationConstraintValidator.validate("listId", listId, constraints));
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
		return validate((ChecklistEntryBean) object);
	}
}
