package de.benjaminborbe.message.dao;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageValidator extends ValidatorBase<MessageBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public MessageValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<MessageBean> getType() {
		return MessageBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<MessageBean>> buildRules() {
		final Map<String, ValidatorRule<MessageBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<MessageBean>() {

				@Override
				public Collection<ValidationError> validate(final MessageBean bean) {
					final MessageIdentifier value = bean.getId();
					final List<ValidationConstraint<MessageIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<MessageIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<MessageIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// type
		{
			final String field = "type";
			result.put(field, new ValidatorRule<MessageBean>() {

				@Override
				public Collection<ValidationError> validate(final MessageBean bean) {
					final String value = bean.getType();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
