package de.benjaminborbe.distributed.search.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringCharacter;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class DistributedSearchPageValidator implements Validator<DistributedSearchPageBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public DistributedSearchPageValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<DistributedSearchPageBean> getType() {
		return DistributedSearchPageBean.class;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((DistributedSearchPageBean) object);
	}

	@Override
	public Collection<ValidationError> validate(final DistributedSearchPageBean bean) {
		final Set<ValidationError> result = new HashSet<ValidationError>();

		{
			final String index = bean.getIndex();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintIndexLetters());
			result.addAll(validationConstraintValidator.validate("index", index, constraints));
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

	private final class ValidationConstraintIndexLetters extends ValidationConstraintStringCharacter {

		@Override
		protected boolean isAllowedCharacter(final char character) {
			return Character.isUpperCase(character) || Character.isLowerCase(character) || '-' == character;
		}

	}
}
