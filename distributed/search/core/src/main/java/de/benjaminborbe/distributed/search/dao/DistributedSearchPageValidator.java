package de.benjaminborbe.distributed.search.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringCharacter;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;

import javax.inject.Inject;
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
		final Map<String, ValidatorRule<DistributedSearchPageBean>> result = new HashMap<String, ValidatorRule<DistributedSearchPageBean>>();

		// index
		{
			final String field = "index";
			result.put(field, new ValidatorRule<DistributedSearchPageBean>() {

				@Override
				public Collection<ValidationError> validate(final DistributedSearchPageBean bean) {
					final String value = bean.getIndex();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
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
