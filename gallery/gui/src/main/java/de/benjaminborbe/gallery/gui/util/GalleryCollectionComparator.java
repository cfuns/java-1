package de.benjaminborbe.gallery.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.tools.util.ComparatorChain;

public class GalleryCollectionComparator extends ComparatorChain<GalleryCollection> {

	@Inject
	public GalleryCollectionComparator(final GalleryCollectionComparatorName name, final GalleryCollectionComparatorPrio prio) {
		super(prio, name);
	}

}
