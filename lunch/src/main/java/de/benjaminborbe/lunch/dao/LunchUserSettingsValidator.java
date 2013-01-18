package de.benjaminborbe.lunch.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

public class LunchUserSettingsValidator implements Validator<LunchUserSettingsBean> {

	private class ValidationConstraintUsername implements ValidationConstraint<LunchUserSettingsIdentifier> {

		private final String[] disallowedStrings;

		public ValidationConstraintUsername(final String... disallowedStrings) {
			this.disallowedStrings = disallowedStrings;
		}

		@Override
		public boolean precondition(final LunchUserSettingsIdentifier object) {
			return object != null;
		}

		@Override
		public boolean validate(final LunchUserSettingsIdentifier object) {
			for (final String disallowedString : disallowedStrings) {
				if (disallowedString.equals(object.getId())) {
					return false;
				}
			}
			return true;
		}
	}

	private final ValidationConstraintValidator validationConstraintValidator;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public LunchUserSettingsValidator(final Logger logger, final ValidationConstraintValidator validationConstraintValidator, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.validationConstraintValidator = validationConstraintValidator;
		this.authenticationService = authenticationService;
	}

	@Override
	public Class<LunchUserSettingsBean> getType() {
		return LunchUserSettingsBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final LunchUserSettingsBean object) {
		final LunchUserSettingsBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		{
			final LunchUserSettingsIdentifier id = bean.getId();
			final List<ValidationConstraint<LunchUserSettingsIdentifier>> constraints = new ArrayList<ValidationConstraint<LunchUserSettingsIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<LunchUserSettingsIdentifier>());
			constraints.add(new ValidationConstraintUsername("alle", "root", "admin") {

			});
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}

		{
			final UserIdentifier owner = bean.getOwner();
			try {
				if (!authenticationService.existsUser(owner)) {
					result.add(new ValidationErrorSimple("unkown user " + owner));
				}
			}
			catch (final AuthenticationServiceException e) {
				logger.warn(e.getClass().getName(), e);
				result.add(new ValidationErrorSimple("validate user failed"));
			}
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((LunchUserSettingsBean) object);
	}
}
