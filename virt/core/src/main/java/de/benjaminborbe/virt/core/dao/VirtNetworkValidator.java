package de.benjaminborbe.virt.core.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtNetworkValidator extends ValidatorBase<VirtNetworkBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public VirtNetworkValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<VirtNetworkBean> getType() {
		return VirtNetworkBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<VirtNetworkBean>> buildRules() {
		final Map<String, ValidatorRule<VirtNetworkBean>> result = new HashMap<String, ValidatorRule<VirtNetworkBean>>();

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<VirtNetworkBean>() {

				@Override
				public Collection<ValidationError> validate(final VirtNetworkBean bean) {
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
			result.put(field, new ValidatorRule<VirtNetworkBean>() {

				@Override
				public Collection<ValidationError> validate(final VirtNetworkBean bean) {
					final VirtNetworkIdentifier value = bean.getId();
					final List<ValidationConstraint<VirtNetworkIdentifier>> constraints = new ArrayList<ValidationConstraint<VirtNetworkIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<VirtNetworkIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
