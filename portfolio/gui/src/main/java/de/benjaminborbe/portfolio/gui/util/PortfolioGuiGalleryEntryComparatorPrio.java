package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PortfolioGuiGalleryEntryComparatorPrio extends ComparatorBase<GalleryEntry, Long> {

	@Override
	public Long getValue(final GalleryEntry o) {
		return o.getPriority();
	}

	@Override
	public boolean nullFirst() {
		return false;
	}

	@Override
	public boolean inverted() {
		return true;
	}

}
