package de.benjaminborbe.gallery.gui.util;

import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.tools.util.ComparatorBase;

public class GalleryGroupComparator extends ComparatorBase<GalleryGroup, String> {

	@Override
	public String getValue(final GalleryGroup o) {
		return o.getName();
	}

}
