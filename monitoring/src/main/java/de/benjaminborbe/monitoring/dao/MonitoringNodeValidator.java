package de.benjaminborbe.monitoring.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class MonitoringNodeValidator extends ValidatorBase<MonitoringNodeBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public MonitoringNodeValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<MonitoringNodeBean> getType() {
		return MonitoringNodeBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<MonitoringNodeBean>> buildRules() {
		final Map<String, ValidatorRule<MonitoringNodeBean>> result = new HashMap<String, ValidatorRule<MonitoringNodeBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final MonitoringNodeIdentifier value = bean.getId();
					final List<ValidationConstraint<MonitoringNodeIdentifier>> constraints = new ArrayList<ValidationConstraint<MonitoringNodeIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<MonitoringNodeIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
