package de.benjaminborbe.note.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

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

		return result;
	}
}
