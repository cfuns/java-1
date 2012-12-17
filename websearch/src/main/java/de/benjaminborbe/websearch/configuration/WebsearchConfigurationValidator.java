package de.benjaminborbe.websearch.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

public class WebsearchConfigurationValidator implements Validator<WebsearchConfigurationBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public WebsearchConfigurationValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<WebsearchConfigurationBean> getType() {
		return WebsearchConfigurationBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final WebsearchConfigurationBean object) {
		final WebsearchConfigurationBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();
		{
			final Boolean activated = bean.getActivated();
			final List<ValidationConstraint<Boolean>> constraints = new ArrayList<ValidationConstraint<Boolean>>();
			constraints.add(new ValidationConstraintNotNull<Boolean>());
			result.addAll(validationConstraintValidator.validate("activated", activated, constraints));
		}
		{
			final Long delay = bean.getDelay();
			final List<ValidationConstraint<Long>> constraints = new ArrayList<ValidationConstraint<Long>>();
			constraints.add(new ValidationConstraintNotNull<Long>());
			constraints.add(new ValidationConstraintLongGE(0));
			constraints.add(new ValidationConstraintLongLE(10000));
			result.addAll(validationConstraintValidator.validate("delay", delay, constraints));
		}
		{
			final List<String> excludes = bean.getExcludes();
			final List<ValidationConstraint<List<String>>> constraints = new ArrayList<ValidationConstraint<List<String>>>();
			constraints.add(new ValidationConstraintNotNull<List<String>>());
			result.addAll(validationConstraintValidator.validate("excludes", excludes, constraints));
		}
		{
			final Integer expire = bean.getExpire();
			final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
			constraints.add(new ValidationConstraintNotNull<Integer>());
			constraints.add(new ValidationConstraintIntegerGE(0));
			constraints.add(new ValidationConstraintIntegerLE(365));
			result.addAll(validationConstraintValidator.validate("expire", expire, constraints));
		}
		{
			final UserIdentifier owner = bean.getOwner();
			final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<ValidationConstraint<UserIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
			result.addAll(validationConstraintValidator.validate("owner", owner, constraints));
		}
		{
			final URL url = bean.getUrl();
			final List<ValidationConstraint<URL>> constraints = new ArrayList<ValidationConstraint<URL>>();
			constraints.add(new ValidationConstraintNotNull<URL>());
			result.addAll(validationConstraintValidator.validate("url", url, constraints));
		}
		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((WebsearchConfigurationBean) object);
	}
}
