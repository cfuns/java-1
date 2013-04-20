package de.benjaminborbe.gallery.gui.util;

import javax.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorChain;

public class GalleryEntryComparator extends ComparatorChain<GalleryEntry> {

	@Inject
	public GalleryEntryComparator(final GalleryEntryComparatorName name, final GalleryEntryComparatorPrio prio) {
		super(prio, name);
	}

}
