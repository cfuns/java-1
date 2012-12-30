package de.benjaminborbe.authentication.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringEmail;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class UserValidator implements Validator<UserBean> {

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

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((UserBean) object);
	}

	@Override
	public Collection<ValidationError> validate(final UserBean bean) {
		final Set<ValidationError> result = new HashSet<ValidationError>();
		try {

			// id
			{
				final UserIdentifier id = bean.getId();
				if (id == null || id.getId() == null || id.getId().length() == 0) {
					result.add(new ValidationErrorSimple("login missing"));
				}
				else if (usernameHasInvalidCharacter(id.getId())) {
					result.add(new ValidationErrorSimple("login contains invalid character"));
				}
				else if (bean.getCreated() == null && userDao.exists(id)) {
					final String msg = "user " + id + " already exists";
					result.add(new ValidationErrorSimple(msg));
				}
			}

			// email
			{
				final String email = bean.getEmail();
				final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
				constraints.add(new ValidationConstraintNotNull<String>());
				constraints.add(new ValidationConstraintStringMinLength(1));
				constraints.add(new ValidationConstraintStringMaxLength(255));
				constraints.add(new ValidationConstraintStringEmail());
				result.addAll(validationConstraintValidator.validate("email", email, constraints));
			}
		}
		catch (final StorageException e) {
			result.add(new ValidationErrorSimple(e.getClass().getName() + ", validation failed!"));
		}
		return result;
	}

	public boolean usernameHasInvalidCharacter(final String username) {
		for (final char c : username.toCharArray()) {
			if ('a' > c || c > 'z') {
				return true;
			}
		}
		return false;
	}
}
