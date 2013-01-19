package de.benjaminborbe.gallery.validation;

import java.util.HashMap;
import java.util.Map;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;

public class GalleryEntryValidator extends ValidatorBase<GalleryEntryBean> {

	@Override
	public Class<GalleryEntryBean> getType() {
		return GalleryEntryBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<GalleryEntryBean>> buildRules() {
		final Map<String, ValidatorRule<GalleryEntryBean>> result = new HashMap<String, ValidatorRule<GalleryEntryBean>>();
		return result;
	}

}
