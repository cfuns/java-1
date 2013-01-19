package de.benjaminborbe.authentication.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringEmail;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

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
						}
						else if (usernameHasInvalidCharacter(value.getId())) {
							result.add(new ValidationErrorSimple("login contains invalid character"));
						}
						else if (bean.getCreated() == null && userDao.exists(value)) {
							final String msg = "user " + value + " already exists";
							result.add(new ValidationErrorSimple(msg));
						}
					}
					catch (final StorageException e) {
						result.add(new ValidationErrorSimple("StorageException"));
					}
					return result;
				}
			});
		}

		// email
		{
			final String field = "email";
			result.put(field, new ValidatorRule<UserBean>() {

				@Override
				public Collection<ValidationError> validate(final UserBean bean) {
					final String value = bean.getEmail();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintStringEmail());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
