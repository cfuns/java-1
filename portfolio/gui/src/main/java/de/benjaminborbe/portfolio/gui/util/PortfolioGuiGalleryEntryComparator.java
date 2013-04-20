package de.benjaminborbe.portfolio.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorChain;

public class PortfolioGuiGalleryEntryComparator extends ComparatorChain<GalleryEntry> {

	@Inject
	public PortfolioGuiGalleryEntryComparator(final PortfolioGuiGalleryEntryComparatorName name, final PortfolioGuiGalleryEntryComparatorPrio prio) {
		super(prio, name);
	}

}
