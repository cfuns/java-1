package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PortfolioGuiGalleryEntryComparatorName extends ComparatorBase<GalleryEntry, String> {

	@Override
	public String getValue(final GalleryEntry o) {
		return o.getName();
	}

}
