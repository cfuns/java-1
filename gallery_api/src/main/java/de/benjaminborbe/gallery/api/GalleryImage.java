package de.benjaminborbe.gallery.api;

public interface GalleryImage {

	String getName();

	byte[] getContent();

	GalleryImageIdentifier getId();

	String getContentType();

}
