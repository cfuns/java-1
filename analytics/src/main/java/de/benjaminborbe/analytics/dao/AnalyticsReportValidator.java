package de.benjaminborbe.analytics.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class AnalyticsReportValidator implements Validator<AnalyticsReportBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public AnalyticsReportValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<AnalyticsReportBean> getType() {
		return AnalyticsReportBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final AnalyticsReportBean bean) {
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// id
		{
			final AnalyticsReportIdentifier id = bean.getId();
			final List<ValidationConstraint<AnalyticsReportIdentifier>> constraints = new ArrayList<ValidationConstraint<AnalyticsReportIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<AnalyticsReportIdentifier>());
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}

		// aggregation
		{
			final AnalyticsReportAggregation aggregation = bean.getAggregation();
			final List<ValidationConstraint<AnalyticsReportAggregation>> constraints = new ArrayList<ValidationConstraint<AnalyticsReportAggregation>>();
			constraints.add(new ValidationConstraintNotNull<AnalyticsReportAggregation>());
			result.addAll(validationConstraintValidator.validate("aggregation", aggregation, constraints));
		}
		// name
		{
			final String name = bean.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((AnalyticsReportBean) object);
	}

}
