package de.benjaminborbe.distributed.search.dao;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringCharacter;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributedSearchPageValidator extends ValidatorBase<DistributedSearchPageBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public DistributedSearchPageValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<DistributedSearchPageBean> getType() {
		return DistributedSearchPageBean.class;
	}

	public boolean usernameHasInvalidCharacter(final String username) {
		for (final char c : username.toCharArray()) {
			if ('a' > c || c > 'z') {
				return true;
			}
		}
		return false;
	}

	private final class ValidationConstraintIndexLetters extends ValidationConstraintStringCharacter {

		@Override
		protected boolean isAllowedCharacter(final char character) {
			return Character.isUpperCase(character) || Character.isLowerCase(character) || '-' == character;
		}
	}

	@Override
	protected Map<String, ValidatorRule<DistributedSearchPageBean>> buildRules() {
		final Map<String, ValidatorRule<DistributedSearchPageBean>> result = new HashMap<>();

		// index
		{
			final String field = "index";
			result.put(field, new ValidatorRule<DistributedSearchPageBean>() {

				@Override
				public Collection<ValidationError> validate(final DistributedSearchPageBean bean) {
					final String value = bean.getIndex();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintIndexLetters());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
