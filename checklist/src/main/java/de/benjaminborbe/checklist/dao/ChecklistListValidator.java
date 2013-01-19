package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class ChecklistListValidator extends ValidatorBase<ChecklistListBean> {

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
	protected Map<String, ValidatorRule<ChecklistListBean>> buildRules() {
		final Map<String, ValidatorRule<ChecklistListBean>> result = new HashMap<String, ValidatorRule<ChecklistListBean>>();

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<ChecklistListBean>() {

				@Override
				public Collection<ValidationError> validate(final ChecklistListBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<ChecklistListBean>() {

				@Override
				public Collection<ValidationError> validate(final ChecklistListBean bean) {
					final ChecklistListIdentifier value = bean.getId();
					final List<ValidationConstraint<ChecklistListIdentifier>> constraints = new ArrayList<ValidationConstraint<ChecklistListIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<ChecklistListIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<ChecklistListBean>() {

				@Override
				public Collection<ValidationError> validate(final ChecklistListBean bean) {
					final UserIdentifier value = bean.getOwner();
					final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<ValidationConstraint<UserIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
