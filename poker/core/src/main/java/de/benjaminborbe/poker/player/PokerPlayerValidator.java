package de.benjaminborbe.poker.player;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

import javax.inject.Inject;
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
		final Map<String, ValidatorRule<PokerPlayerBean>> result = new HashMap<String, ValidatorRule<PokerPlayerBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<PokerPlayerBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerPlayerBean bean) {
					final PokerPlayerIdentifier value = bean.getId();
					final List<ValidationConstraint<PokerPlayerIdentifier>> constraints = new ArrayList<ValidationConstraint<PokerPlayerIdentifier>>();
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
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
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
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
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
