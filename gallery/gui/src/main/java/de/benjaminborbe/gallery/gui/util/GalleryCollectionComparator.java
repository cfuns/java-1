package de.benjaminborbe.gallery.gui.util;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class GalleryCollectionComparator extends ComparatorChain<GalleryCollection> {

	@SuppressWarnings("unchecked")
	@Inject
	public GalleryCollectionComparator(final GalleryCollectionComparatorName name, final GalleryCollectionComparatorPrio prio) {
		super(prio, name);
	}

}
