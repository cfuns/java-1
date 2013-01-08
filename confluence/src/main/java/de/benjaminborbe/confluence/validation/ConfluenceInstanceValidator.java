package de.benjaminborbe.confluence.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGT;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringUrl;

public class ConfluenceInstanceValidator implements Validator<ConfluenceInstanceBean> {

	private final UrlUtil urlUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final ConfluenceConnector confluenceConnector;

	private final Logger logger;

	@Inject
	public ConfluenceInstanceValidator(
			final Logger logger,
			final UrlUtil urlUtil,
			final ValidationConstraintValidator validationConstraintValidator,
			final ConfluenceConnector confluenceConnector) {
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
	public Collection<ValidationError> validate(final ConfluenceInstanceBean bean) {
		final Set<ValidationError> result = new HashSet<ValidationError>();

		{
			final Integer expire = bean.getExpire();
			final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
			constraints.add(new ValidationConstraintNotNull<Integer>());
			constraints.add(new ValidationConstraintIntegerGT(0));
			result.addAll(validationConstraintValidator.validate("expire", expire, constraints));
		}

		{
			final String url = bean.getUrl();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringUrl(urlUtil));
			result.addAll(validationConstraintValidator.validate("url", url, constraints));
		}

		{
			final String username = bean.getUsername();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("username", username, constraints));
		}

		{
			final String password = bean.getPassword();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("password", password, constraints));
		}

		if (Boolean.TRUE.equals(bean.getActivated())) {
			final String url = bean.getUrl();
			final String username = bean.getUsername();
			final String password = bean.getPassword();
			try {
				logger.debug("validate login - url: '" + url + "' username: '" + username + "' password: '****'");
				confluenceConnector.login(url, username, password);
			}
			catch (final Exception e) {
				logger.debug(e.getClass().getName(), e);
				result.add(new ValidationErrorSimple("login failed to confluence"));
			}
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
		return validate((ConfluenceInstanceBean) object);
	}

}
