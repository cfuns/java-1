package de.benjaminborbe.wiki.dao;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringNot;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiSpaceValidator extends ValidatorBase<WikiSpaceBean> {

	private final class ValidationConstrainAllowedCharacters implements ValidationConstraint<String> {

		@Override
		public boolean precondition(final String object) {
			return object != null;
		}

		@Override
		public boolean validate(final String object) {
			for (final char c : object.toCharArray()) {
				if (!isValidCharacter(c)) {
					return false;
				}
			}
			return true;
		}
	}

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public WikiSpaceValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	private boolean isValidCharacter(final char c) {
		if (Character.isLetterOrDigit(c) || c == ' ') {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Class<WikiSpaceBean> getType() {
		return WikiSpaceBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<WikiSpaceBean>> buildRules() {
		final Map<String, ValidatorRule<WikiSpaceBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<WikiSpaceBean>() {

				@Override
				public Collection<ValidationError> validate(final WikiSpaceBean bean) {
					final WikiSpaceIdentifier value = bean.getId();
					final List<ValidationConstraint<WikiSpaceIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<WikiSpaceIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<WikiSpaceIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<WikiSpaceBean>() {

				@Override
				public Collection<ValidationError> validate(final WikiSpaceBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstrainAllowedCharacters());
					constraints.add(new ValidationConstraintStringNot("all", "none"));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
