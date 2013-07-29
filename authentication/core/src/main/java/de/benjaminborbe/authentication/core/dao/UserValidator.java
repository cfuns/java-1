package de.benjaminborbe.authentication.core.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringEmail;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.storage.api.StorageException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserValidator extends ValidatorBase<UserBean> {

	private final UserDao userDao;

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public UserValidator(final ValidationConstraintValidator validationConstraintValidator, final UserDao userDao) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.userDao = userDao;
	}

	@Override
	public Class<UserBean> getType() {
		return UserBean.class;
	}

	public boolean usernameHasInvalidCharacter(final String username) {
		for (final char c : username.toCharArray()) {
			if ('a' > c || c > 'z') {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Map<String, ValidatorRule<UserBean>> buildRules() {
		final Map<String, ValidatorRule<UserBean>> result = new HashMap<String, ValidatorRule<UserBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<UserBean>() {

				@Override
				public Collection<ValidationError> validate(final UserBean bean) {
					final Set<ValidationError> result = new HashSet<ValidationError>();
					try {
						final UserIdentifier value = bean.getId();
						if (value == null || value.getId() == null || value.getId().length() == 0) {
							result.add(new ValidationErrorSimple("login missing"));
						} else if (usernameHasInvalidCharacter(value.getId())) {
							result.add(new ValidationErrorSimple("login contains invalid character"));
						} else if (bean.getCreated() == null && userDao.exists(value)) {
							final String msg = "user " + value + " already exists";
							result.add(new ValidationErrorSimple(msg));
						}
					} catch (final StorageException e) {
						result.add(new ValidationErrorSimple("StorageException"));
					}
					return result;
				}
			});
		}

		// email+emailNew
		{
			final String field = "email";
			result.put(field, new ValidatorRule<UserBean>() {

				@Override
				public Collection<ValidationError> validate(final UserBean bean) {
					final String emailValue = bean.getEmail();
					final List<ValidationConstraint<String>> emailConstraints = new ArrayList<ValidationConstraint<String>>();
					emailConstraints.add(new ValidationConstraintNotNull<String>());
					emailConstraints.add(new ValidationConstraintStringMinLength(1));
					emailConstraints.add(new ValidationConstraintStringMaxLength(255));
					emailConstraints.add(new ValidationConstraintStringEmail());
					final Collection<ValidationError> emailResult = validationConstraintValidator.validate(field, emailValue, emailConstraints);

					final String emailNewValue = bean.getEmailNew();
					final List<ValidationConstraint<String>> emailNewConstraints = new ArrayList<ValidationConstraint<String>>();
					emailNewConstraints.add(new ValidationConstraintNotNull<String>());
					emailNewConstraints.add(new ValidationConstraintStringMinLength(1));
					emailNewConstraints.add(new ValidationConstraintStringMaxLength(255));
					emailNewConstraints.add(new ValidationConstraintStringEmail());
					final Collection<ValidationError> emailNewResult = validationConstraintValidator.validate(field, emailNewValue, emailNewConstraints);

					if (emailNewResult.isEmpty()) {
						return emailNewResult;
					} else {
						return emailResult;
					}
				}
			});
		}

		// emailNew
		{
			final String field = "emailNew";
			result.put(field, new ValidatorRule<UserBean>() {

				@Override
				public Collection<ValidationError> validate(final UserBean bean) {
					final String value = bean.getEmailNew();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintOr<String>().add(new ValidationConstraintNull<String>()).add(
						new ValidationConstraintAnd<String>().add(new ValidationConstraintStringMinLength(1)).add(new ValidationConstraintStringMaxLength(255))
							.add(new ValidationConstraintStringEmail())));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
