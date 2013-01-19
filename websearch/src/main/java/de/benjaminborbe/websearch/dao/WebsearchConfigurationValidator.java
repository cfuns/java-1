package de.benjaminborbe.websearch.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

public class WebsearchConfigurationValidator extends ValidatorBase<WebsearchConfigurationBean> {

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
	protected Map<String, ValidatorRule<WebsearchConfigurationBean>> buildRules() {
		final Map<String, ValidatorRule<WebsearchConfigurationBean>> result = new HashMap<String, ValidatorRule<WebsearchConfigurationBean>>();

		// activated
		{
			final String field = "activated";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final Boolean value = bean.getActivated();
					final List<ValidationConstraint<Boolean>> constraints = new ArrayList<ValidationConstraint<Boolean>>();
					constraints.add(new ValidationConstraintNotNull<Boolean>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// delay
		{
			final String field = "delay";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final Long value = bean.getDelay();
					final List<ValidationConstraint<Long>> constraints = new ArrayList<ValidationConstraint<Long>>();
					constraints.add(new ValidationConstraintNotNull<Long>());
					constraints.add(new ValidationConstraintLongGE(0));
					constraints.add(new ValidationConstraintLongLE(10000));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// excludes
		{
			final String field = "excludes";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final List<String> value = bean.getExcludes();
					final List<ValidationConstraint<List<String>>> constraints = new ArrayList<ValidationConstraint<List<String>>>();
					constraints.add(new ValidationConstraintNotNull<List<String>>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// expire
		{
			final String field = "expire";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final Integer value = bean.getExpire();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintNotNull<Integer>());
					constraints.add(new ValidationConstraintIntegerGE(0));
					constraints.add(new ValidationConstraintIntegerLE(365));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final UserIdentifier value = bean.getOwner();
					final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<ValidationConstraint<UserIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// url
		{
			final String field = "url";
			result.put(field, new ValidatorRule<WebsearchConfigurationBean>() {

				@Override
				public Collection<ValidationError> validate(final WebsearchConfigurationBean bean) {
					final URL value = bean.getUrl();
					final List<ValidationConstraint<URL>> constraints = new ArrayList<ValidationConstraint<URL>>();
					constraints.add(new ValidationConstraintNotNull<URL>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
