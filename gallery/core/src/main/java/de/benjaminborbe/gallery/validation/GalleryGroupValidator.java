package de.benjaminborbe.gallery.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringOnlyLetters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryGroupValidator extends ValidatorBase<GalleryGroupBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public GalleryGroupValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<GalleryGroupBean> getType() {
		return GalleryGroupBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<GalleryGroupBean>> buildRules() {
		final Map<String, ValidatorRule<GalleryGroupBean>> result = new HashMap<>();

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintStringOnlyLetters());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}

}
