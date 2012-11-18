package de.benjaminborbe.portfolio.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

public class GalleryComparator extends ComparatorChain<GalleryCollection> {

	@SuppressWarnings("unchecked")
	@Inject
	public GalleryComparator(final GalleryComparatorName galleryComparatorName, final GalleryComparatorPrio galleryComparatorPrio) {
		super(galleryComparatorPrio, galleryComparatorName);
	}

}
