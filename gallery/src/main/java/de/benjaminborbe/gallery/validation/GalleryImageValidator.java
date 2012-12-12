package de.benjaminborbe.gallery.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.gallery.dao.GalleryImageBean;
import de.benjaminborbe.tools.validation.Validator;

public class GalleryImageValidator implements Validator<GalleryImageBean> {

	@Override
	public Class<GalleryImageBean> getType() {
		return GalleryImageBean.class;
	}

	public Collection<ValidationError> validate(final GalleryImageBean object) {
		final GalleryImageBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate content
		{
			final byte[] content = bean.getContent();
			if (content == null || content.length == 0) {
				result.add(new ValidationErrorSimple("image-content missing"));
			}
		}

		// validate contentType
		{
			final String contentType = bean.getContentType();
			if (contentType == null || contentType.length() == 0) {
				result.add(new ValidationErrorSimple("image-contentType missing"));
			}
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((GalleryImageBean) object);
	}
}
