package de.benjaminborbe.gallery.api;

public interface GalleryImage {

	String getName();

	GalleryImageIdentifier getId();

	byte[] getContent();

	String getContentType();

	GalleryIdentifier getGalleryIdentifier();

}
