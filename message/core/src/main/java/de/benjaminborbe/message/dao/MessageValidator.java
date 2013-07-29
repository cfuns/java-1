package de.benjaminborbe.message.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.message.api.MessageIdentifier;

import javax.inject.Inject;
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
		final Map<String, ValidatorRule<MessageBean>> result = new HashMap<String, ValidatorRule<MessageBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<MessageBean>() {

				@Override
				public Collection<ValidationError> validate(final MessageBean bean) {
					final MessageIdentifier value = bean.getId();
					final List<ValidationConstraint<MessageIdentifier>> constraints = new ArrayList<ValidationConstraint<MessageIdentifier>>();
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
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
