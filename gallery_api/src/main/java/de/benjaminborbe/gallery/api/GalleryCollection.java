package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryCollection {

	GalleryCollectionIdentifier getId();

	String getName();

	Calendar getCreated();

	Calendar getModified();

	GalleryGroupIdentifier getGroupId();

	Long getPriority();

	Boolean getShared();
}
