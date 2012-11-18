package de.benjaminborbe.gallery.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.tools.validation.Validator;

public class GalleryGroupValidator implements Validator<GalleryGroupBean> {

	@Override
	public Class<GalleryGroupBean> getType() {
		return GalleryGroupBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final Object object) {
		final GalleryGroupBean bean = (GalleryGroupBean) object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		final String name = bean.getName();
		{
			if (name == null || name.length() == 0) {
				result.add(new ValidationErrorSimple("name missing"));
			}
		}

		return result;
	}

}
