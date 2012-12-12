package de.benjaminborbe.gallery.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringOnlyLetters;

public class GalleryGroupValidator implements Validator<GalleryGroupBean> {

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
	public Collection<ValidationError> validate(final GalleryGroupBean object) {
		final GalleryGroupBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		{
			final String name = bean.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringOnlyLetters());
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((GalleryGroupBean) object);
	}

}
