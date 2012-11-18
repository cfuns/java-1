package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryGroup {

	GalleryGroupIdentifier getId();

	String getName();

	Calendar getCreated();

	Calendar getModified();
}
