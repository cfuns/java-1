package de.benjaminborbe.dhl.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintLongGT;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DhlValidator extends ValidatorBase<DhlBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public DhlValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<DhlBean> getType() {
		return DhlBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<DhlBean>> buildRules() {
		final Map<String, ValidatorRule<DhlBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<DhlBean>() {

				@Override
				public Collection<ValidationError> validate(final DhlBean bean) {
					final DhlIdentifier value = bean.getId();
					final List<ValidationConstraint<DhlIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<DhlIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<DhlIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<DhlBean>() {

				@Override
				public Collection<ValidationError> validate(final DhlBean bean) {
					final UserIdentifier value = bean.getOwner();
					final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<UserIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// trackingNumber
		{
			final String field = "trackingNumber";
			result.put(field, new ValidatorRule<DhlBean>() {

				@Override
				public Collection<ValidationError> validate(final DhlBean bean) {
					final String value = bean.getTrackingNumber();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// zip
		{
			final String field = "zip";
			result.put(field, new ValidatorRule<DhlBean>() {

				@Override
				public Collection<ValidationError> validate(final DhlBean bean) {
					final Long value = bean.getZip();
					final List<ValidationConstraint<Long>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<Long>());
					constraints.add(new ValidationConstraintLongGT(0));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
