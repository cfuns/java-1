package de.benjaminborbe.portfolio.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

public class PortfolioGuiGalleryComparator extends ComparatorChain<GalleryCollection> {

	@SuppressWarnings("unchecked")
	@Inject
	public PortfolioGuiGalleryComparator(final PortfolioGuiGalleryComparatorName galleryComparatorName, final PortfolioGuiGalleryComparatorPrio galleryComparatorPrio) {
		super(galleryComparatorPrio, galleryComparatorName);
	}

}
