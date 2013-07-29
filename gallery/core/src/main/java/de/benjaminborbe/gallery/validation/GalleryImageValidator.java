package de.benjaminborbe.gallery.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.dao.GalleryImageBean;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintByteArrayMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryImageValidator extends ValidatorBase<GalleryImageBean> {

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
	protected Map<String, ValidatorRule<GalleryImageBean>> buildRules() {
		final Map<String, ValidatorRule<GalleryImageBean>> result = new HashMap<String, ValidatorRule<GalleryImageBean>>();

		// content
		{
			final String field = "content";
			result.put(field, new ValidatorRule<GalleryImageBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryImageBean bean) {
					final byte[] value = bean.getContent();
					final List<ValidationConstraint<byte[]>> constraints = new ArrayList<ValidationConstraint<byte[]>>();
					constraints.add(new ValidationConstraintNotNull<byte[]>());
					constraints.add(new ValidationConstraintByteArrayMinLength(1));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// contentType
		{
			final String field = "contentType";
			result.put(field, new ValidatorRule<GalleryImageBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryImageBean bean) {
					final String value = bean.getContentType();
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
