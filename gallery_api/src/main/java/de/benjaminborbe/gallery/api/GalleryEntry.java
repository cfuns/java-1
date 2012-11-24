package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryEntry {

	GalleryEntryIdentifier getId();

	GalleryCollectionIdentifier getCollectionId();

	String getName();

	GalleryImageIdentifier getImageIdentifier();

	GalleryImageIdentifier getPreviewImageIdentifier();

	Calendar getCreated();

	Calendar getModified();

	Long getPriority();

	Boolean getShared();

}
