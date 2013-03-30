package de.benjaminborbe.virt.core.dao;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;

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
		final Map<String, ValidatorRule<VirtNetworkBean>> result = new HashMap<>();

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<VirtNetworkBean>() {

				@Override
				public Collection<ValidationError> validate(final VirtNetworkBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
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
					final List<ValidationConstraint<VirtNetworkIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<VirtNetworkIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
