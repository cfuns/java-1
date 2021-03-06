package de.benjaminborbe.note.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.note.api.NoteIdentifier;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteValidator extends ValidatorBase<NoteBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public NoteValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<NoteBean> getType() {
		return NoteBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<NoteBean>> buildRules() {
		final Map<String, ValidatorRule<NoteBean>> result = new HashMap<String, ValidatorRule<NoteBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<NoteBean>() {

				@Override
				public Collection<ValidationError> validate(final NoteBean bean) {
					final NoteIdentifier value = bean.getId();
					final List<ValidationConstraint<NoteIdentifier>> constraints = new ArrayList<ValidationConstraint<NoteIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<NoteIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<NoteIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// owner
		{
			final String field = "owner";
			result.put(field, new ValidatorRule<NoteBean>() {

				@Override
				public Collection<ValidationError> validate(final NoteBean bean) {
					final UserIdentifier value = bean.getOwner();
					final List<ValidationConstraint<UserIdentifier>> constraints = new ArrayList<ValidationConstraint<UserIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<UserIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<UserIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// title
		{
			final String field = "title";
			result.put(field, new ValidatorRule<NoteBean>() {

				@Override
				public Collection<ValidationError> validate(final NoteBean bean) {
					final String value = bean.getTitle();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// content
		{
			final String field = "content";
			result.put(field, new ValidatorRule<NoteBean>() {

				@Override
				public Collection<ValidationError> validate(final NoteBean bean) {
					final String value = bean.getContent();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
