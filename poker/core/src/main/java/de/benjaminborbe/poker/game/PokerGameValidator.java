package de.benjaminborbe.poker.game;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerGameValidator extends ValidatorBase<PokerGameBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public PokerGameValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<PokerGameBean> getType() {
		return PokerGameBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<PokerGameBean>> buildRules() {
		final Map<String, ValidatorRule<PokerGameBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<PokerGameBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerGameBean bean) {
					final PokerGameIdentifier value = bean.getId();
					final List<ValidationConstraint<PokerGameIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<PokerGameIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<PokerGameIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<PokerGameBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerGameBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
