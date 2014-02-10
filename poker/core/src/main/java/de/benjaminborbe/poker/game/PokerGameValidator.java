package de.benjaminborbe.poker.game;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintLongGT;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.poker.api.PokerGameIdentifier;

import javax.inject.Inject;
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
		final Map<String, ValidatorRule<PokerGameBean>> result = new HashMap<String, ValidatorRule<PokerGameBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<PokerGameBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerGameBean bean) {
					final PokerGameIdentifier value = bean.getId();
					final List<ValidationConstraint<PokerGameIdentifier>> constraints = new ArrayList<ValidationConstraint<PokerGameIdentifier>>();
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
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// startCredit
		{
			final String field = "startCredit";
			result.put(field, new ValidatorRule<PokerGameBean>() {

				@Override
				public Collection<ValidationError> validate(final PokerGameBean bean) {
					final Long value = bean.getStartCredits();
					final List<ValidationConstraint<Long>> constraints = new ArrayList<ValidationConstraint<Long>>();
					constraints.add(new ValidationConstraintNotNull<Long>());
					constraints.add(new ValidationConstraintLongGT(0));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
