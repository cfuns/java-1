package de.benjaminborbe.confluence.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerGT;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringUrl;
import de.benjaminborbe.tools.url.UrlUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfluenceInstanceValidator extends ValidatorBase<ConfluenceInstanceBean> {

	private final UrlUtil urlUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final ConfluenceConnector confluenceConnector;

	private final Logger logger;

	@Inject
	public ConfluenceInstanceValidator(
		final Logger logger,
		final UrlUtil urlUtil,
		final ValidationConstraintValidator validationConstraintValidator,
		final ConfluenceConnector confluenceConnector
	) {
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.validationConstraintValidator = validationConstraintValidator;
		this.confluenceConnector = confluenceConnector;
	}

	@Override
	public Class<ConfluenceInstanceBean> getType() {
		return ConfluenceInstanceBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<ConfluenceInstanceBean>> buildRules() {
		final Map<String, ValidatorRule<ConfluenceInstanceBean>> result = new HashMap<>();

		// expire
		{
			final String field = "expire";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final Integer value = bean.getExpire();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<Integer>());
					constraints.add(new ValidationConstraintIntegerGT(0));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// url
		{
			final String field = "url";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final String value = bean.getUrl();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintStringUrl());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// username
		{
			final String field = "username";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final String value = bean.getUsername();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// password
		{
			final String field = "password";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final String value = bean.getPassword();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// activated
		{
			final String field = "activated";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final Set<ValidationError> result = new HashSet<>();
					if (Boolean.TRUE.equals(bean.getActivated())) {
						final String url = bean.getUrl();
						final String username = bean.getUsername();
						final String password = bean.getPassword();
						try {
							logger.debug("validate login - url: '" + url + "' username: '" + username + "' password: '****'");
							confluenceConnector.login(url, username, password);
						} catch (final Exception e) {
							logger.debug(e.getClass().getName(), e);
							result.add(new ValidationErrorSimple("login failed to confluence"));
						}

					}
					return result;
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<ConfluenceInstanceBean>() {

				@Override
				public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
					final UserIdentifier value = bean.getOwner();
					final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
