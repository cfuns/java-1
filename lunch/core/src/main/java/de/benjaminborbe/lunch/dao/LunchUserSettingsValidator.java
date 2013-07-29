package de.benjaminborbe.lunch.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LunchUserSettingsValidator extends ValidatorBase<LunchUserSettingsBean> {

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
	public LunchUserSettingsValidator(
		final Logger logger,
		final ValidationConstraintValidator validationConstraintValidator,
		final AuthenticationService authenticationService
	) {
		this.logger = logger;
		this.validationConstraintValidator = validationConstraintValidator;
		this.authenticationService = authenticationService;
	}

	@Override
	public Class<LunchUserSettingsBean> getType() {
		return LunchUserSettingsBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<LunchUserSettingsBean>> buildRules() {
		final Map<String, ValidatorRule<LunchUserSettingsBean>> result = new HashMap<String, ValidatorRule<LunchUserSettingsBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<LunchUserSettingsBean>() {

				@Override
				public Collection<ValidationError> validate(final LunchUserSettingsBean bean) {
					final LunchUserSettingsIdentifier value = bean.getId();
					final List<ValidationConstraint<LunchUserSettingsIdentifier>> constraints = new ArrayList<ValidationConstraint<LunchUserSettingsIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<LunchUserSettingsIdentifier>());
					constraints.add(new ValidationConstraintUsername("alle", "root", "admin"));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<LunchUserSettingsBean>() {

				@Override
				public Collection<ValidationError> validate(final LunchUserSettingsBean bean) {
					final Set<ValidationError> result = new HashSet<ValidationError>();
					final UserIdentifier owner = bean.getOwner();
					try {
						if (!authenticationService.existsUser(owner)) {
							result.add(new ValidationErrorSimple("unkown user " + owner));
						}
					} catch (final AuthenticationServiceException e) {
						logger.warn(e.getClass().getName(), e);
						result.add(new ValidationErrorSimple("validate user failed"));
					}
					return result;
				}
			});
		}

		return result;
	}
}
