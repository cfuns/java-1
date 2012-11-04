package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.tools.util.ComparatorBase;

public class GalleryComparator extends ComparatorBase<Gallery, String> {

	@Override
	public String getValue(final Gallery o) {
		return o.getName();
	}
}
