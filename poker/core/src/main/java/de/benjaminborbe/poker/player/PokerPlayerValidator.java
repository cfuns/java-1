package de.benjaminborbe.poker.player;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
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

public class PokerPlayerValidator extends ValidatorBase<PokerPlayerBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public PokerPlayerValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<PokerPlayerBean> getType() {
		return PokerPlayerBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<PokerPlayerBean>> buildRules() {
		final Map<String, ValidatorRule<PokerPlayerBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<PokerPlayerBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerPlayerBean bean) {
					final PokerPlayerIdentifier value = bean.getId();
					final List<ValidationConstraint<PokerPlayerIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<PokerPlayerIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<PokerPlayerIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<PokerPlayerBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerPlayerBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// token
		{
			final String field = "token";
			result.put(field, new ValidatorRule<PokerPlayerBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerPlayerBean bean) {
					final String value = bean.getToken();
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
