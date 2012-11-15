package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryImage {

	GalleryImageIdentifier getId();

	byte[] getContent();

	String getContentType();

	Calendar getCreated();

	Calendar getModified();

	String getName();

}
