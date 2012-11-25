package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PortfolioGuiGalleryComparatorName extends ComparatorBase<GalleryCollection, String> {

	@Override
	public String getValue(final GalleryCollection o) {
		return o.getName();
	}

	@Override
	public boolean inverted() {
		return false;
	}

	@Override
	public boolean nullFirst() {
		return false;
	}

}
