package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class PortfolioGuiGalleryCollectionComparator extends ComparatorChain<GalleryCollection> {

	@SuppressWarnings("unchecked")
	@Inject
	public PortfolioGuiGalleryCollectionComparator(
		final PortfolioGuiGalleryCollectionComparatorName galleryComparatorName,
		final PortfolioGuiGalleryCollectionComparatorPrio galleryComparatorPrio
	) {
		super(galleryComparatorPrio, galleryComparatorName);
	}

}
