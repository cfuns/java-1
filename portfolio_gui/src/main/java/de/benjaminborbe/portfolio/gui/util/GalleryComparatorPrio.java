package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorBase;

public class GalleryComparatorPrio extends ComparatorBase<GalleryCollection, String> {

	@Override
	public String getValue(final GalleryCollection o) {
		return o.getName();
	}
}
