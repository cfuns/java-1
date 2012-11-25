package de.benjaminborbe.portfolio.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorChain;

public class PortfolioGuiGalleryEntryComparator extends ComparatorChain<GalleryEntry> {

	@SuppressWarnings("unchecked")
	@Inject
	public PortfolioGuiGalleryEntryComparator(final PortfolioGuiGalleryEntryComparatorName name, final PortfolioGuiGalleryEntryComparatorPrio prio) {
		super(prio, name);
	}

}
