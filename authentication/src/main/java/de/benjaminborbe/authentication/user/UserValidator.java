package de.benjaminborbe.authentication.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.validation.Validator;

public class UserValidator implements Validator<UserBean> {

	@Inject
	public UserValidator() {
	}

	@Override
	public Class<UserBean> getType() {
		return UserBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final Object object) {
		final UserBean bean = (UserBean) object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		final UserIdentifier id = bean.getId();
		{
			if (id == null || id.getId() == null || id.getId().length() == 0) {
				result.add(new ValidationErrorSimple("login missing"));
			}
			else if (usernameHasInvalidCharacter(id.getId())) {
				result.add(new ValidationErrorSimple("login contains invalid character"));
			}
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
