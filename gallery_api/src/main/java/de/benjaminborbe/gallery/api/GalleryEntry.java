package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryEntry {

	GalleryEntryIdentifier getId();

	String getName();

	GalleryImageIdentifier getImageIdentifier();

	GalleryImageIdentifier getPreviewImageIdentifier();

	Calendar getCreated();

	Calendar getModified();

}
