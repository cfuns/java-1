package de.benjaminborbe.gallery.gui.util;

import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class GalleryEntryComparator extends ComparatorChain<GalleryEntry> {

	@Inject
	public GalleryEntryComparator(final GalleryEntryComparatorName name, final GalleryEntryComparatorPrio prio) {
		super(prio, name);
	}

}
