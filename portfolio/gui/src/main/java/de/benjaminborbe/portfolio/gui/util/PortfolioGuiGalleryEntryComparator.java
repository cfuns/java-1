package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class PortfolioGuiGalleryEntryComparator extends ComparatorChain<GalleryEntry> {

	@SuppressWarnings("unchecked")
	@Inject
	public PortfolioGuiGalleryEntryComparator(final PortfolioGuiGalleryEntryComparatorName name, final PortfolioGuiGalleryEntryComparatorPrio prio) {
		super(prio, name);
	}

}
