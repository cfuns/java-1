package de.benjaminborbe.gallery.validation;

import de.benjaminborbe.gallery.dao.GalleryEntryBean;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;

import java.util.HashMap;
import java.util.Map;

public class GalleryEntryValidator extends ValidatorBase<GalleryEntryBean> {

	@Override
	public Class<GalleryEntryBean> getType() {
		return GalleryEntryBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<GalleryEntryBean>> buildRules() {
		final Map<String, ValidatorRule<GalleryEntryBean>> result = new HashMap<>();
		return result;
	}

}
