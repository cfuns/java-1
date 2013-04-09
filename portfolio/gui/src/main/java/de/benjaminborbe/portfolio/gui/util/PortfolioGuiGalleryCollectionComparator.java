package de.benjaminborbe.portfolio.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

public class PortfolioGuiGalleryCollectionComparator extends ComparatorChain<GalleryCollection> {

	@Inject
	public PortfolioGuiGalleryCollectionComparator(
			final PortfolioGuiGalleryCollectionComparatorName galleryComparatorName,
			final PortfolioGuiGalleryCollectionComparatorPrio galleryComparatorPrio) {
		super(galleryComparatorPrio, galleryComparatorName);
	}

}
