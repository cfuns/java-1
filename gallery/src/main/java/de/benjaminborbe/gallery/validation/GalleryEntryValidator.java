package de.benjaminborbe.gallery.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;
import de.benjaminborbe.tools.validation.Validator;

public class GalleryEntryValidator implements Validator<GalleryEntryBean> {

	@Override
	public Class<GalleryEntryBean> getType() {
		return GalleryEntryBean.class;
	}

	public Collection<ValidationError> validate(final GalleryEntryBean object) {
		final Set<ValidationError> result = new HashSet<ValidationError>();
		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((GalleryEntryBean) object);
	}

}
