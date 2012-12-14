package de.benjaminborbe.gallery.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.dao.GalleryImageBean;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintByteArrayMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;

public class GalleryImageValidator implements Validator<GalleryImageBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public GalleryImageValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<GalleryImageBean> getType() {
		return GalleryImageBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final GalleryImageBean object) {
		final GalleryImageBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate content
		{
			final byte[] content = bean.getContent();
			final List<ValidationConstraint<byte[]>> constraints = new ArrayList<ValidationConstraint<byte[]>>();
			constraints.add(new ValidationConstraintNotNull<byte[]>());
			constraints.add(new ValidationConstraintByteArrayMinLength(1));
			result.addAll(validationConstraintValidator.validate("content", content, constraints));
		}

		// validate contentType
		{
			final String contentType = bean.getContentType();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("contentType", contentType, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((GalleryImageBean) object);
	}
}
