package de.benjaminborbe.wiki.dao;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPageValidator extends ValidatorBase<WikiPageBean> {

	private final class ValidationConstrainAllowedCharacters implements ValidationConstraint<String> {

		@Override
		public boolean precondition(final String object) {
			return object != null;
		}

		@Override
		public boolean validate(final String object) {
			for (final char c : object.toCharArray()) {
				if (!isAllowedCharacter(c)) {
					return false;
				}
			}
			return true;
		}

		private boolean isAllowedCharacter(final char c) {
			if (Character.isLetter(c)) {
				return true;
			}
			if (c == '-') {
				return true;
			}
			if (c == ' ') {
				return true;
			}
			return false;
		}
	}

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public WikiPageValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<WikiPageBean> getType() {
		return WikiPageBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<WikiPageBean>> buildRules() {
		final Map<String, ValidatorRule<WikiPageBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<WikiPageBean>() {

				@Override
				public Collection<ValidationError> validate(final WikiPageBean bean) {
					final WikiPageIdentifier value = bean.getId();
					final List<ValidationConstraint<WikiPageIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<WikiPageIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<WikiPageIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<WikiPageBean>() {

				@Override
				public Collection<ValidationError> validate(final WikiPageBean bean) {
					final String value = bean.getTitle();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstrainAllowedCharacters());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
